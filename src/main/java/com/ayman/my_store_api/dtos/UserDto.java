package com.ayman.my_store_api.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
@AllArgsConstructor
@Getter
public class UserDto
{
    private Long id;
    private String name;
    private String email;

}
