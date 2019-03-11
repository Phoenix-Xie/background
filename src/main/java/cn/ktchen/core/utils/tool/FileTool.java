package cn.ktchen.core.utils.tool;

import cn.ktchen.core.config.properties.UploadProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;

/**
 * @ClassName FileTool
 * @Description 文件工具，获取项目路径，获取文件后缀名
 * @Author alex
 * Date 2019-02-27 00:23
 * Version 1.0
 **/
@Component
public class FileTool {

    /**
     * 上传文件
     * @param inputStream 文件流
     * @param path 上传路径
     * @throws IOException
     */
    public static void upload(InputStream inputStream, String path) throws IOException {
        // 检查本地文件上传是否启动
        if (!UploadProperties.isOpen()) {
            throw new RuntimeException("文件上传已关闭");
        }

        // 检查文件大小
        if (inputStream.available() > UploadProperties.getMaxFileSize()) {
            throw new RuntimeException("文件过大");
        }

        // 检查文件是否存在
        String realPath = getProjectPath() + UploadProperties.getUpload() + path;
        File file = new File(realPath);
        checkAndMakeDirs(file);

        // 写入文件
        byte[] buffer = new byte[4 * 1024];
        try(OutputStream os = Files.newOutputStream(file.toPath())) {
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        }
    }


    /**
     * 检查文件是否存在并创建文件目录
     * @param file
     * @throws IOException
     */
    public static void checkAndMakeDirs(File file) throws IOException{
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }else {
            throw new IOException("File already exists");
        }
    }

    /**
     * 获取项目不同模式下的根路径
     */
    public static String getProjectPath(){
        String filePath = FileTool.class.getResource("").getPath();
        String projectPath = FileTool.class.getClassLoader().getResource("").getPath();
        StringBuilder path = new StringBuilder();

        if(!filePath.startsWith("file:/")){
            // 开发模式下根路径
            char[] filePathArray = filePath.toCharArray();
            char[] projectPathArray = projectPath.toCharArray();
            for (int i = 0; i < filePathArray.length; i++) {
                if(projectPathArray.length > i && filePathArray[i] == projectPathArray[i]){
                    path.append(filePathArray[i]);
                }else {
                    break;
                }
            }
        }else if(!projectPath.startsWith("file:/")){
            // 部署服务器模式下根路径
            projectPath = projectPath.replace("/WEB-INF/classes/", "");
            projectPath = projectPath.replace("/target/classes/", "");
            try {
                path.append(URLDecoder.decode(projectPath,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return projectPath;
            }
        }else {
            // jar包启动模式下根路径
            String property = System.getProperty("java.class.path");
            int firstIndex = property.lastIndexOf(System.getProperty("path.separator")) + 1;
            int lastIndex = property.lastIndexOf(File.separator) + 1;
            path.append(property, firstIndex, lastIndex);
            System.out.println(path);
        }

        File file = new File(path.toString());
        String rootPath = "/";
        try {
            rootPath = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rootPath.replaceAll("\\\\","/");
    }

    /**
     * 获取文件后缀名
     */
    public static String getFileSuffix(String fileName) {
        if(!StringUtils.isEmpty(fileName)){
            int lastIndexOf = fileName.lastIndexOf(".");
            return fileName.substring(lastIndexOf);
        }
        return "";
    }
}
