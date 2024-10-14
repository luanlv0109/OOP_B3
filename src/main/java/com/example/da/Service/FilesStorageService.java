package com.example.da.Service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FilesStorageService {

    public void init();


    public String save(MultipartFile file) throws IOException;

    public Resource load(String filename) throws MalformedURLException;
}
