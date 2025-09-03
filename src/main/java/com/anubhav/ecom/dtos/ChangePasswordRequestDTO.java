package com.anubhav.ecom.dtos;

import lombok.Data;

@Data
public class ChangePasswordRequestDTO
{
    private String oldPassword;
    private String newPassword;
}
