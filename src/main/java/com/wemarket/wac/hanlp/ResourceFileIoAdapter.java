package com.wemarket.wac.hanlp;

import com.hankcs.hanlp.corpus.io.IIOAdapter;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * Created by justinli on 2019/2/15
 **/
public class ResourceFileIoAdapter implements IIOAdapter {
    @Override
    public InputStream open(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource( path );
        InputStream is = new FileInputStream( resource.getFile() );
        return is;
    }

    @Override
    public OutputStream create(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource( path );
        OutputStream os = new FileOutputStream( resource.getFile() );
        return os;
    }
}
