package utils;


import bussiness.SystemConfigService;
import org.apache.commons.io.FileUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.VisitorSupport;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-11
 * Time: 下午2:52
 * 功能描述: 上传时用创建目录
 */
public class QicFileUtil {
    public static void copyFile(File src ,File dist) throws IOException {
        FileUtils.copyFile(src,dist);
    }
    public  static void copyFileToDirectory(File src,String distDir) throws IOException{
        FileUtils.copyFileToDirectory(src,new File(distDir));
    }
    /**
     * 保存导入用户的excel文件到临时目录
     * 返回文件路径
     *
     * @param file
     * @return
     */
    public static void saveUserExcelTotmp(File file) throws Exception {

        copyFileToDirectory(file, SystemConfigService.get(Constants.USER_EXCEL_UPLOAD_TEMP_DIR));
    }

    /**
     * 保存导入用户的excel文件到正式目录
     * 返回文件路径
     *
     * @return
     */
    public static String saveUserExcelToOfficai(File tmpFile, File officalFile) {
        return null;
    }

    /**
     * 保存策略文件到临时目录
     * 返回文件路径
     *
     * @param file
     * @return Map
     * key说明:
     *
     *
     */
    public static Map<String,Object> saveStrategyToTmp(File file) throws Exception {
        return unStrategyZipAndSave(file);
    }

    /**
     * 保存策略文件到正式目录
     * @return
     */
    public static void saveStrategyToOfficai(File tmpFile,String strageName) throws Exception {
         FileUtils.copyFileToDirectory(tmpFile,new File(SystemConfigService.get(Constants.STRATEGY_UPLOAD_OFFICIAL_DIR) + strageName));
    }

    /**
     * 保存策略文件到正式目录
     * @param tmpFilePath
     * @throws Exception
     */
    public static void saveStrategyToOfficai(String tmpFilePath,String strageName) throws Exception {
        saveStrategyToOfficai(new File(tmpFilePath),strageName);
    }

    /**
     * 删除文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if(file.exists()){
          return  file.delete();
        }
        return false;
    }
    /**
     * 删除文件
     *
     * @return
     */
    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    /**
     * 拼接形成文件上传目录
     * 基本目录+年/月/日组成文件最终存放的路径
     *
     * @param key
     * @return
     */
    private static String pinDir(String key) {
        String baseDir = QicConfigProperties.getString(key);
        String dynamicDir = String.format(String.format("%1$ty" + File.separator + "%1$tm" + File.separator + "%1$te" + File.separator, System.currentTimeMillis()));
        return baseDir + dynamicDir;
    }

    /**
     * 解压文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static Map<String,Object> unStrategyZipAndSave(File file) throws Exception {
        List<String> files = new ArrayList<String>(2);
        Map<String,Object> returnMap = new HashMap<String,Object>();
        Map<String,ZipEntry> map = new HashMap<String,ZipEntry>();
        ZipFile zip = new ZipFile(file);
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            if(!entry.isDirectory()){//如果
                if(isXml(zipEntryName)){
                    map.put("xml",entry);
                }else if(isDll(zipEntryName)){
                    map.put("dll",entry);
                }
               // FileUtils.copyFileToDirectory();
            }else{//如果entry是文件jia 则先写到tmp下 再遍历
                continue;
            }
        }
        if(map.size()<2){
            returnMap.put("fileNotFound",true);
            return returnMap;
        }else {//判断两个文件名是否相同

        }
        //写文件
        //先解析校验xml文件
        InputStream is = zip.getInputStream(map.get("xml"));
        SAXReader xmlReader = new SAXReader();
        parseXml(xmlReader.read(is),returnMap);//解析后的数据会存储到map中
        if(!validXml(returnMap)){
            returnMap.put("fileErr",true);
            return returnMap;
        }else{
            //解析时间
            try{
                String[] playBackDate =((String)returnMap.get("PlayBackDate")).split("_");
                String[] playBackTime =((String)returnMap.get("PlayBackTime")).split("_");
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                returnMap.put("BackTestStartDate",format2.format(format.parse(playBackDate[0] + playBackTime[0])));
                returnMap.put("BackTestEndtDate",format2.format(format.parse(playBackDate[1] + playBackTime[1])));
            }catch (Exception e){
                e.printStackTrace();
                returnMap.put("fileErr",true);
                return returnMap;
            }

        }
        //开始复制文件
        for(ZipEntry zipEntry:map.values()){
            InputStream in = zip.getInputStream(zipEntry);
            int index1 = zipEntry.getName().lastIndexOf("\\");//用File.sperator 不生效
            int index2= zipEntry.getName().lastIndexOf("/");
            String suffixDir  = (index1 - index2) != 0 ? zipEntry.getName().substring(0,index1>index2?index1:index2):"";
            String fileName = (index1 - index2) != 0? zipEntry.getName().substring(index1>index2?index1+1:index2+1):zipEntry.getName();
            //策略文件最终保存的路目录为基目录加策略各
           // suffixDir = "".equals(suffixDir)?fileName.substring(0,fileName.indexOf(".")) : suffixDir;
            String filePath = copyFileToDirectoty(in,SystemConfigService.get(Constants.STRATEGY_UPLOAD_TEMP_DIR) + fileName.substring(0,fileName.indexOf(".")),fileName);
            if(filePath != null){
              files.add(filePath);
            }else{
                returnMap.put("fileExist",true);
                return returnMap;
            }
        }
        returnMap.put("files",files);
        return returnMap;
    }
    private static String copyFileToDirectoty(InputStream in,String directory,String fileName) throws IOException {

        //看看正式文件库中有没有该策略
        File officalFile = new File(SystemConfigService.get(Constants.STRATEGY_UPLOAD_OFFICIAL_DIR),fileName);
        if(officalFile.exists()){//文件已存在
            return null;
        }
        File newFileDir = new File(directory);
        if(!newFileDir.isDirectory()){
            newFileDir.mkdirs();
        }

        File newFile = new File(newFileDir,fileName);
        FileOutputStream fos = new FileOutputStream(newFile);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            fos.write(buf, 0, len);
        }
        in.close();
        fos.close();
       return newFile.getAbsolutePath();
    }

    private static boolean isDll(String fileName) {
        return fileName == null ? false : fileName.endsWith(".dll");
    }

    private static boolean isXml(String fileName) {
        return fileName == null ? false : fileName.endsWith(".xml");
    }

    /**
     * 解析xml
     * @param document
     * @param map
     * @return
     */
    private static Map<String,Object> parseXml(Document document,final Map<String,Object> map) {
        /**
         * 以访问者模式遍历xml，这里只需重写属性访问的方法
         */
        final List<String> attubutes = Arrays.asList(new String[]{"Author","CreateTime","StrategyName","Instruction"});
        document.getRootElement().accept(new VisitorSupport() {

            @Override
            public void visit(Attribute attribute){

                if(attubutes.contains(attribute.getName().trim())){
                    map.put(attribute.getName(),attribute.getValue());
                }
                if(attribute.getName().equals("key")&&attribute.getValue().equals("InitFund") ){
                    map.put("InitFund",attribute.getParent().attribute("value").getValue());
                }

                if(attribute.getName().equals("key")&&attribute.getValue().equals("PlayBackDate") ){
                    map.put("PlayBackDate",attribute.getParent().attribute("value").getValue());
                }
                if(attribute.getName().equals("key")&&attribute.getValue().equals("PlayBackTime") ){
                    map.put("PlayBackTime",attribute.getParent().attribute("value").getValue());
                }

            }
        });


        return map;
    }

        /**
         * 校验初始是否有初始资金和回验时间
         * @param map
         * @return
         */
        private static boolean  validXml(Map<String,Object> map){
            if(map == null){
                return false;
            }else{
                return map.containsKey("PlayBackDate") && map.containsKey("InitFund");
            }
        }


}
