package com.cts.fse.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("user")
public class User{
    @Id
    private String loginId;
    private String email;
    private String name;
    private String password;
    private String contactNo;
    private boolean isUserActive;
    private String role;
    private Ticket bookedTicket;
    private String token;

}
