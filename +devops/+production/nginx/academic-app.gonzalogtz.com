server {
    listen 80;
    server_name academic-app.gonzalogtz.com;

    # Redirect HTTP to HTTPS
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name academic-app.gonzalogtz.com;

    # SSL certificates (managed by certbot)
    ssl_certificate /etc/letsencrypt/live/academic-app.gonzalogtz.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/academic-app.gonzalogtz.com/privkey.pem;
    include /etc/letsencrypt/options-ssl-nginx.conf;
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

    location / {
        proxy_pass http://127.0.0.1:3080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
