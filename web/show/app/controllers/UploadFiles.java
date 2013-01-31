package controllers;

import annotation.QicFunction;
import play.Logger;
import utils.Constants;
import utils.QicFileUtil;
import utils.SystemResponseMessage;

import java.io.File;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-6
 * Time: 上午10:19
 * 功能描述: 上传文件的写在这里
 */
public class UploadFiles extends BasePlayControllerSupport {

    /**
     * 上传策略包   正式环境linux注意文件写权限问题
     * 1. 解压.zip包
     * 2.检查目录下是否一个dll文件和一个xml文件，并且两文件名相同
     * 3. 解析xml文件
     * 4. 将两文件copy到临时目录
     *
     * @param attachment
     */
    @QicFunction(id=7)
    public static void uploadStrage(File attachment) {
        try {
            if(attachment == null){
                setSuccessFlag(false);
                setMessage(SystemResponseMessage.UPLOAD_FILE_EMPTY_RSP);
                renderJSON(getSampleResponseMap());
                return;
            }
            if(attachment.length() > Constants.MAX_SIZE_OF_ZIP_STRATEGY_FILE){
                setSuccessFlag(false);
                setMessage(String.format(SystemResponseMessage.UPLOAD_FILE_OUT_OF_SIZE_RSP,Constants.MAX_SIZE_OF_ZIP_STRATEGY_FILE>>20));
                renderJSON(getSampleResponseMap());
            }
            if(!attachment.getName().endsWith(".zip")){
                setSuccessFlag(false);
                setMessage("文件类型错误，只能上传zip压缩文件");
                renderJSON(getSampleResponseMap());
            }


            //保存文件到临时目录
            Map<String, Object> map = QicFileUtil.saveStrategyToTmp(attachment);
            //将文件名返回到客户端
            getSampleResponseMap().putAll(map);

        } catch (Exception e) {
            setSuccessFlag(false);//出错了
            setMessage(SystemResponseMessage.SYSTEM_DEFAULT_ERR_RSP);
            Logger.error(e.getMessage(), e);
        }
        renderJSON(getSampleResponseMap());

    }

}
