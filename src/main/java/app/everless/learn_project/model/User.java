package app.everless.learn_project.model;

import lombok.Data;

@Data
public class User {
    @TableFielId("id")
    private int id;
    private String userName;
}
