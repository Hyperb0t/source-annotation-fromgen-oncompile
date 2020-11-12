package ru.itis.annotation;


@HtmlForm(method = "post", action = "/users")
public class User {
    @FormField(type = "text", name = "first_name", placeholder = "Имя")
    private String firstName;
    @FormField(type = "text", name = "last_name", placeholder = "Фамилия")
    private String lastName;
    @FormField(type = "email", name = "email", placeholder = "Email")
    private String email;
    @FormField(type = "password", name = "password", placeholder = "Пароль")
    private String password;
}
