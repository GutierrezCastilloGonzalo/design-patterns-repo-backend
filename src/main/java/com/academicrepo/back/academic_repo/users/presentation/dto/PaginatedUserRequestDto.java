package com.academicrepo.back.academic_repo.users.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedUserRequestDto {
    private Integer page = 1;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDirection = "asc";

    public void normalizePageNumber() {
        if (this.page != null && this.page > 0) {
            this.page = this.page - 1;
        }
    }
}
