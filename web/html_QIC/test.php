<?php
//echo 11111;
$dataValue  = $_POST['data-value'];
//if($dataValue == 0001){
	$liHtml = '<li class="display" class="for_tab">稳健型股票推2<img src="images/stable_close.png" class="stable_close"></li>';
	$contHtml = '<li class="display">
                            
                            <div class="stable_info">
                               <h3 class="common_til_h3">
                                    广发证券稳健型股票推荐
                                    <span class="my_comment">
                                        <span class="add_collect">&nbsp;</span>
                                     <img width="93" height="26" class="stable_comment" src="images/my_comment.png">
                                    </span>
                               </h3> 
                               <div class="stable_txt">
                           			<table width="100%" class="" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="8%" align="right"> 组合收益：</td>
    <td width="14%">6%</td>
    <td width="20%">组合评级：<span class="star_b1" style="width:87px;">&nbsp;</span></td>
    <td width="58%">选出股票：10</td>
  </tr>
  <tr>
    <td align="right">组合来源：</td>
    <td colspan="3">广州证券顶级策略 </td>
  </tr>
  <tr>
    <td align="right">更新频率：</td>
    <td colspan="3">研报每天更新，股票池每天更新调整</td>
  </tr>
  <tr>
    <td align="right">组合说明：</td>
    <td colspan="3">基于对当前大盘及行业的判断，选取适合稳健型投资者的相关的个股。</td>
  </tr>
  <tr>
    <td colspan="4">
         <div class="attachment_research">
            <img src="images/attachment.png">&nbsp;&nbsp;<img src="images/research.png">
    	</div>
    </td>
  </tr>
</table>
								</div>
                                
                           </div>
                            
                <table width="100%" cellspacing="0" cellpadding="0" border="0" class="tbl_exec">
  <tr>
    <th width="10%">组合名称</th>
    <th width="10%">来源</th>
    <th width="10%">年化收益率</th>
    <th width="10%">夏普比率</th>
    <th width="10%">用户评级</th>
    <th width="10%">更新日期</th>
    <th width="10%"> 组合股票数</th>
    <th width="10%">收藏人气</th>
    <th width="10%">我要收藏</th>
    <th width="10%">我要评价</th>
    </tr>
    </table>
    
                                                                   
                <div class="tbl_scoll">
            
                <table width="100%" cellspacing="0" cellpadding="0" border="0" class="tbl_common tbl_exec tbl_no_scroll">
            
               
              <tr>
                <td width="10%"><a href="#">稳健型股票推荐</a></td>
                <td width="10%">稳化证券</td>
                <td width="10%">0.01%</td>
                <td width="10%"><span class="blue">0.00</span></td>
                <td width="10%"><span style="width:87px;" class="star_b1">&nbsp;</span></td>
                <td width="10%">2012—12-12</td>
                <td width="10%">333个</td>
                <td width="10%"><span class="blue">22</span></td>
                 <td width="10%"><span class="btn_collect">我要收藏</span></td>
                <td width="10%"><span class="btn_comment">我要评价</span></td>
                </tr>
                
                <tr>
                <td width="10%"><a href="#">稳健型股票推荐</a></td>
                <td width="10%">稳化证券</td>
                <td width="10%">0.01%</td>
                <td width="10%"><span class="blue">0.00</span></td>
                <td width="10%"><span style="width:87px;" class="star_b1">&nbsp;</span></td>
                <td width="10%">2012—12-12</td>
                <td width="10%">333个</td>
                <td width="10%"><span class="blue">22</span></td>
                 <td width="10%"><span class="btn_uncollect">我要收藏</span></td>
                <td width="10%"><span class="btn_uncomment">我要评价</span></td>
                </tr>
                </tr>
                
              <tr>
                <td width="10%"><a href="#">稳健型股票推荐</a></td>
                <td width="10%">稳化证券</td>
                <td width="10%">0.01%</td>
                <td width="10%">0.00</td>
                <td width="10%">五星</td>
                <td width="10%">2012—12-12</td>
                <td width="10%">333</td>
                <td width="10%">22</td>
                 <td width="10%">我要收藏</td>
                <td width="10%">我要评价</td>
                </tr>
                
                 <tr>
                <td width="10%"><a href="#">稳健型股票推荐</a></td>
                <td width="10%">稳化证券</td>
                <td width="10%">0.01%</td>
                <td width="10%">0.00</td>
                <td width="10%">五星</td>
                <td width="10%">2012—12-12</td>
                <td width="10%">333</td>
                <td width="10%">22</td>
                 <td width="10%">我要收藏</td>
                <td width="10%">我要评价</td>
                </tr>
      
            </table>
            </div>
                        </li>';
						
	/**/
	$arr = array('liHtml'=>$liHtml, 'contHtml' => $contHtml);
	
	echo json_encode($arr);
	//echo 'add';
//}

?>