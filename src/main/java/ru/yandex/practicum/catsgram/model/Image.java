package ru.yandex.practicum.catsgram.model;

import lombok.Data;

@Data
public class Image {
    private Long id;
    private Long postId;
    private String filename;
    private String filePath;
}
