<?php
$html = '<form method="post" class="form_new_user">

<table width="100%" class="tbl_new_user" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="20%" align="right">  <span class="red">*</span>姓名：</td>
    <td width="80%">
           <p class="p_valid">
            <input type="text"  name="fullname" class="new_input">
           </p>
            <font class="new_user_tips p_l_10 nu_two">
                        <span class="tips_ico">&nbsp;</span>
                       	<span class="tips_text">2～12位中文、字母、数字、下划线</span>
                     </font>
    </td>
  </tr>
  <tr>
    <td align="right"><span class="red">*</span>帐号：</td>
    <td>
        	<p class="p_valid">
            <input type="text" id="username" name="username" class="new_input">
           </p>
            <font class="new_user_tips p_l_10 nu_two">
                        <span class="tips_ico">&nbsp;</span>
                       	<span class="tips_text tips_error">账号输入错误</span>
            </font>
        </td>
  </tr>
  <tr>
    <td align="right"><span class="red">*</span>密码：</td>
    <td>
    	 <p class="p_valid">
            <input type="text" class="new_input">
         </p>
            <font class="new_user_tips p_l_10 nu_two">
                        <span class="tips_ico">&nbsp;</span>
                       	<span class="tips_text tips_ok">正确</span>
           </font>
    	
       </td>
  </tr>
  <tr>
    <td align="right"><span class="red">*</span>再次输入密码：</td>
    <td><input type="text" class="new_input">
    </td>
  </tr>
  <tr>
    <td align="right">联系电话：</td>
    <td><input type="text" class="new_input"></td>
  </tr>
  <tr>
    <td align="right"><span class="red">*</span>E-mail：</td>
    <td>
        <input type="text" class="new_input">
    </td>
  </tr>
  <tr>
    <td align="right">身份证号：</td>
    <td><input type="text" class="new_input"></td>
  </tr>
  <tr>
    <td align="right">所属营业部：</td>
    <td>
    	<div class="dialog_box">
 
             <input type="hidden" autocomplete="off" id="reportDate_2">
            <input type="hidden" autocomplete="off" id="reportName_2">
             <div id="select_info_year1_2" class="sel_84_dialog new_sel_d">2012</div>
             <ul id="options_year1_2" class="sel_91_option new_sel_option" style="display: none;">
                <li data-value="12">2012</li>
                <li data-value="11">2011</li>
                <li data-value="10">2010</li>
                <li data-value="19">2019</li>
                <li data-value="18">2018</li>
              </ul>
          </div>
    
    </td>
  </tr>
  <tr>
    <td align="right">资金帐号：</td>
    <td><input type="text" class="new_input" /></td>
  </tr>
  <tr>
    <td align="right">联系地址：</td>
    <td><input type="text" class="new_input" /></td>
  </tr>
  <tr>
    <td align="right">邮编：</td>
    <td><input type="text" class="new_input" /></td>
  </tr>
  <tr>
    <td align="right">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
        <div class="new_u_control">
            <input type="reset" value="button" class="new_user_cancle">
            <input type="submit" value="submit" class="new_user_register">
         </div>
</form>';

echo $html;
?>