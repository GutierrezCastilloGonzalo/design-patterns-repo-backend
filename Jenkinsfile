pipeline {
    agent any

    tools {
        jdk 'jdk-21'
    }

    triggers {
        githubPush()
    }

    environment {
        GITHUB_REPO  = 'GutierrezCastilloGonzalo/design-patterns-repo-backend'
        GITHUB_TOKEN = credentials('github-status-token')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    env.GIT_COMMIT_SHA = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                    githubStatus('pending', 'CI pipeline started')
                }
            }
        }

        stage('Compile') {
            steps {
                sh './mvnw compile -B'
            }
        }

        stage('Test + Coverage') {
            steps {
                script {
                    docker.image('postgres:16-alpine').withRun(
                        '-e POSTGRES_DB=academic_repo ' +
                        '-e POSTGRES_USER=academicuser ' +
                        '-e POSTGRES_PASSWORD=academicpass ' +
                        '-p 5432:5432'
                    ) { db ->
                        sh '''
                            # Wait for PostgreSQL to be ready
                            for i in $(seq 1 30); do
                                if docker exec ''' + db.id + ''' pg_isready -U academicuser -d academic_repo; then
                                    echo "PostgreSQL is ready"
                                    break
                                fi
                                echo "Waiting for PostgreSQL... ($i/30)"
                                sleep 2
                            done
                        '''
                        sh './mvnw verify -B -Pcoverage -Dspring.profiles.active=ci'
                    }
                }
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    sh './mvnw sonar:sonar -B -Dsonar.token=$SONAR_TOKEN'
                }
            }
        }

        stage('Package') {
            steps {
                sh './mvnw package -B -DskipTests'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
            }
        }
    }

    post {
        success {
            githubStatus('success', 'CI pipeline passed')
        }
        failure {
            githubStatus('failure', 'CI pipeline failed')
        }
    }
}

def githubStatus(String state, String description) {
    sh """
        curl -s -X POST \
            -H "Authorization: token ${GITHUB_TOKEN}" \
            -H "Content-Type: application/json" \
            -d '{"state":"${state}","description":"${description}","context":"jenkins/ci"}' \
            "https://api.github.com/repos/${GITHUB_REPO}/statuses/${GIT_COMMIT_SHA}"
    """
}
