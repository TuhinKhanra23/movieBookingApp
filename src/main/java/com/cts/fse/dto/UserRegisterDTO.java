package com.cts.fse.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegisterDTO {
    private String loginId;
    private String email;
    private String name;
    private String password;
    private String contactNo;

}
