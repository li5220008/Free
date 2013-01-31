package controllers.external;

import bussiness.UserInfoService;
import controllers.AuthorBaseIntercept;
import dto.FunctionInfoDto;
import play.mvc.Controller;
import play.mvc.With;

import java.util.*;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-24
 * Time: 下午4:03
 * 功能描述:
 */
@With({AuthorBaseIntercept.class})
public class GetFunctions extends Controller {

       public static void getUserFunctions(){
           Map<String, Object> jsonMap = new HashMap<String, Object>();
           jsonMap.put("status",-1);
           try{
               jsonMap.put("status",0);
               long uid = params.get(AuthorBaseIntercept.USER_ID_SESSION_KEY,Long.class );
               List<FunctionInfoDto> list = UserInfoService.findUserFunctionInfoById(uid);
               Set<Long> jsonId = new HashSet<Long>();
               for(FunctionInfoDto dto : list){
                   jsonId.add(dto.id);
                   jsonId.add(dto.pid);
               }
               jsonId.remove(0);
               jsonMap.put("message","获取成功");
               jsonMap.put("data",jsonId);
           }catch (Exception e){
               jsonMap.put("status",-1);
               jsonMap.put("message","请先登入");
             e.printStackTrace();
           }
           renderJSON(jsonMap);
       }
}
