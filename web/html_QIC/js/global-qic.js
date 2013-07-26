/* 
 *func: QIC
 *author:jinwei
 *email: awayInner@gmail.com
 *date: 2012-11-05
 */ 

//正在加载中plugin
;(function($){

	
	$.qicLoading = function(options){
		
		var param = $.extend({
			text: "努力加载中...", //文字
			position: "absolute", //定位
			target: 'body', //加载目标
			top: '40%',
			left: '40%',
			modal: false, //遮罩背景
			width: 220,
			zIndex: 2000, //z-index顺序
			remove: false
		}, options || {});
		
		if(param.remove){
			$(".loading, .bg").remove();
			return false;
		}
		
		//正在加载中图片
		var loadingImg = 'images/loading.gif';
		
		
		//是否需要遮罩背景
		if(param.modal){
			var scrollWidth = $("body")[0].scrollWidth,
				socrllHeight = $("body")[0].scrollHeight;
			
			$(".bg").remove();
			
			$("body").prepend("<div class='bg' style='background-color:#000; opacity:0.5;position:absolute; left:0; top:0; z-index:"
				+ param.zIndex + "; width:"
				+ scrollWidth +"px; height:"
				+ socrllHeight +"px;'></div>");
		}
		
		
		$(".loading").remove();
		
		$(param.target).prepend("<div class='loading' style='text-indent:50px;display: block; border:1px solid #81d0fd; color:#2c81ca; font-size:14px; font-weight:bold; border-radius:2px; line-height:30px; line-height:30px;  box-shadow:0 0 4px #999;  height:30px; z-index:"
		+ (param.zIndex+1)+";width:"
		+ param.width + "px; left:"
		+ param.left + "; top:"
		+ param.top +";background:url("
		+ loadingImg +") no-repeat 20px 3px #fff; position:"
		+ param.position + ";'>"
		+ param.text + "</div>");
	
		
	};

})(jQuery);



//plugin2.0 插件全选or全不选 给jQ对象扩展方法 
;(function($){
	$.fn.extend({
		check: function(){
			return this.each(function(){
				this.checked = true;	
			});	
		},
		uncheck: function(){
			return this.each(function(){
				this.checked = false;	
			});	
		}
		
	});
	
})(jQuery); 

//plugin1.0 提示信息
;(function($){
	$.qicTips = function(param){
		var param = $.extend({
			level: 1,
			messages: '操作成功',
			time: 2000,
			mTop: -30
		
		}, param || {})
	};

	$.extend({
		qicTips: function(){
		
		}
	});

})(jQuery);

/*;(function($){
	jQuery.extend({
		qicTips: function(obj){//操作提示方法
			
			if($(".operate_tips").length == 0){
				var html ='<div class="operate_tips" style="position:absolute;left:500px; z-index:9000;top:100px;  color:#65a5df; height:27px;"><div class="operate_tips_1" style="height:29px;line-height:29px; float:left; position:relative; z-index:50; padding-bottom:1px;background:url(images/operate_sprites_1.png) no-repeat 0px -0px; width:72px;">&nbsp;</div><div class="operate_tips_2" style="height:29px;line-height:29px; float:left; position:relative; z-index:50; padding-bottom:1px;background:url(images/operate_sprites_2.png) repeat-x 0px -0px; z-index:49;position:relative; left:-46px;text-align:center; min-width:42px;max-width:300px;overflow:hidden;white-space:nowrap;">操作成功</div><div class="operate_tips_3" style="height:29px;line-height:29px; float:left; position:relative; z-index:50; padding-bottom:1px;background:url(images/operate_sprites_3.png) no-repeat 0px -0px;position:relative; left:-46px;width:6px;">&nbsp;</div></div>';
				$("body").append(html);
			}
			
			var operTips = $(".operate_tips");
			//取得包含框的子元素(注：children()只考虑子元素而不考虑所有后代元素
			var operTipsChildren = operTips.children("div");
			var operTipsMidTxt = $(".operate_tips_2");
			var time = typeof obj !== 'undefined' ? obj.time : 2000; //停留时间
			var clientX = typeof obj !== 'undefined' ? 
				$(obj.target).offset().left -60 : $("body")[0].clientWidth/2;
			var clientY = typeof obj !== 'undefined' ? 
				$(obj.target).offset().top +30 : $("body")[0].clientHeight/2;	
			
			switch(typeof obj !== 'undefined' ? obj.level : ''){
				case 1:
					operTipsChildren.css({"background-position": '0px -0px'});
					operTipsMidTxt.html(obj.message);
					break;
				case 2:
					operTipsChildren.css({"background-position": '0px -31px', "color": '#db1f10'});
					operTipsMidTxt.html(obj.message);
					break;
				case 3:
					operTipsChildren.css({"background-position": '0px -61px'});
					operTipsMidTxt.html(obj.message);
					break;
				default:
					break	
			}
				
			operTips.css({
					 "position": "absolute",
					 "top": clientY,
					 "left": clientX,
					 "z-index": 90000,
					 'display': 'none',
					 "background-position": '0px 0px',
					 'color': '#65a5df'
				});
			operTips.stop(false, true).show('fast');
				window.setTimeout(function(){
					operTips.stop(false, true).hide('fast');
			}, time); 
		
			
	}
	
   });
})(jQuery);
*/

/*;(function($){
	jQuery.extend({
	qicTips: function(obj){//操作提示方法
			
			if($(".operate_tips").length == 0){
				var html ='<div class="operate_tips" style="background:url(images/operate_sprites.png) no-repeat 0px 0px; color:#65a5df; overflow:hidden; padding:5px 0 0 26px; height:27px; width:82px;">操作成功</div>';
				$("body").append(html);
			}
			
		
			if(typeof obj != 'undefined' && typeof obj.target != 'undefined'){
				var targetLeft = $(obj.target).offset().left -60;
				var targetTop = $(obj.target).offset().top +30;
				//console.log('caa')
			}
			
			var operTips = $(".operate_tips"),
				clientTop = targetTop || ($("body")[0].clientHeight/2),
				clientLeft = targetLeft || ($("body")[0].clientWidth/2);
			
			//console.log(clientTop)
			
			if(typeof obj != 'undefined'){	
				operTips.html(obj.message)
			}
			
			switch(typeof obj=='undefined' ? 'undefined' : obj.level){
				case 1:
					var	bgPosition = '0px -0px';
					break;
				case 2:
					var	bgPosition = '0px -30px';
					var colorTxt = '#db1f10';
					break;	
				case 3:
					var	bgPosition = '0px -61px';
					break;				
			}
			
		
				
			operTips.css({
					 "position": "absolute",
					 "top": clientTop,
					 "left": clientLeft,
					 "z-index": 90000,
					 'display': 'none',
					 "background-position": bgPosition,
					 'color': colorTxt || '#65a5df'
				});
			operTips.stop(false, true).show('fast');
				window.setTimeout(function(){
					operTips.stop(false, true).hide('fast');
			}, 2000); 
			
	}
	});
})(jQuery);*/



/*1.0
 *原生js tab切换
 *func: tabMenu("menuTab", "subCont", "display");
 *参数tabId：鼠标点击要切换的ul的id, subCont：要切换的内容ul的id, display:焦点class
 *一个页面多次调用
 *author: jinwei
 *email: awayInner@gmail.com
 *document.write("星期" + ['一','二','三','四','五','六','日'][new Date().getDay()]);
 */

function _id(id){ 
	return document.getElementById(id);
}		
				
function tabMenu(tabId, contId, display){
	var subCont = _gid(contId); //内容父id
	var subContLi = subCont.children;
	//var subContLi = subCont.getElementsByTagName("li");
	var mentTab = _gid(tabId); //切换菜单父id
	var tabLi = mentTab.children;
	//var tabLi = mentTab.getElementsByTagName("li");
	var arr=[];
	

	
	//获取id的引用
	function _gid(id){ 
		return document.getElementById(id);
	}
	
	//遍历tab的 li
	for(var i=0; i<tabLi.length; i++){
		arr.push(tabLi[i])
		tabLi[i].onclick = function(){
			clearClass(tabLi); //清除菜单tab下的li class
			clearClass(subContLi); //清除内容下的li class
			this.className = display;	
			
			//alert($("li").index(this)); //1.0 jQ 获取当前索引值
			//var curIndex = arr.index(this); //2.0 原生js获取当前索引值
			
			for(var j=0,len=arr.length; j<len; j++){
				if(arr[j] == this){
					curIndex = j;
					break;
				}
			}					
			subContLi[curIndex].className = display;
			
		};
		
	}
	
	//清除所有同胞节点的class
	function clearClass(curenLi){
		for(var j=0; j<curenLi.length; j++){
			curenLi[j].className = '';
		}
	}

}


/*2.0
 *func: 高级搜索，策略对比
 */
function moreSearch(){
	// 2.1搜索设置
	$( "#dialog-link" ).click(function( event ) {
		
		
			$(".search_set_wrap").css("visibility", "visible");
		
			$(".search_set_wrap" ).dialog({
		autoOpen: false,
		width: 660,
		height:430,
		resizable: false,
		overflow: 'inherit',
		modal: true,
		buttons: [
			{
				title: "重置",
				class: 'ser_reset',
				click: function() {
					$( this ).dialog( "close" );
				}
			},
			{
				title: "保存",
				class: 'ser_keep dark_keep',
				click: function() {
					$( this ).dialog( "close" );
				}
			},
			
			{
				title: "搜索",
				class: 'ser_search',
				click: function(){
					$( this ).dialog( "close" );
				}
			}
			
		]
	});
	
	$(".search_set_wrap" ).dialog( "open" );
		event.preventDefault();
	});


	// 2.2.1开始对比
	
	$(".n_contrast").click(function(event){
		var dataArr = [];//对比项
		$(".constract_check").each(function(){
			dataArr.push($(this).find(".constract").attr("data-compare-check"));
		});
		
		if(!(dataArr.length >= 2 && dataArr.length <=5)){
			alert("对比的项数是2-5项!");
			return false;	
		}
		
		
		$(".stategy_constact").css("visibility", "visible");
		$(".stategy_constact").dialog("open");	
		event.preventDefault();
	});
	
	$(".stategy_constact").dialog({
		autoOpen: false,
		modal: true,
		resizable: false,
		width:780,
		height: 590
	})
	
	// 2.2.2要对比的项
	$(".constract_common").click(function(event){//2.3事件委托
		var className = event.target.className; //目标元素class
		var _this = this;
		
		if( className == 'constract'){//添加对比
			var dataCompare = $(event.target).attr("data-compare");
			//console.log($.inArray(dataCompare, compareArr))
			$(event.target).attr("data-compare-check", dataCompare)
			strtegyCompare(_this);
			
		}else if(className=="constract constract_2"){//取消对比
			var dataCompare = $(event.target).attr("data-compare");
			$(event.target).removeAttr("data-compare-check")
			strtegyCompare(_this);
		}

		
		
		//策略对比 背景切换
		function strtegyCompare(_this){
			$(_this).toggleClass("constract_check")	
			$(_this).find(".constract").toggleClass("constract_2")
		}
		
					
		
		// 添加收藏和取消收藏
		if(className == 'add_collect'){
			//添加收藏
			$.get("test.php", {name: 'id'}, function(data){
				alert(data)
				//载入成功时回调函数		
			});
			$(this).find(".add_collect").toggleClass("add_collect_2")
		
		}else if( className == 'add_collect add_collect_2'){
			//取消收藏
			$(this).find(".add_collect").toggleClass("add_collect_2")
		}
		
	});
	
}


/*3.0 UI事件-resize事件
 *Func: 跟随浏览器客户端高度，元素自适应高度
 *For Example: resizeHeight('.right_2_wrap', 475);
 *params: 参素element是元素，trueHeight是已确定元素的高度之和
 */	
function resizeHeight(element, trueHeight){
	var clientHeight = document.documentElement.clientHeight; //浏览器客户区高度
	var hElement = $(element); //自适应高度的元素
	hElement.css({"height": clientHeight- trueHeight});
	$(window).bind("resize", function(){
		hElement.css("height",document.documentElement.clientHeight-trueHeight);
	});
}

/*3.1 
 *Func: 跟随浏览器客户端宽度，元素自适应宽度
 *For Example: resizeWidth('.right_2_wrap', 475);
 *params: 参素element是元素，trueHeight是已确定元素的宽度之和
 */	
function resizeWidth(element, trueWidth){
	var clientWidth = document.documentElement.clientWidth; //浏览器客户区宽度
	var wElement = $(element); //自适应宽度的元素
	wElement.css({"width": clientWidth- trueWidth});
	
	$(window).bind("resize", function(){
		wElement.css("width",document.documentElement.clientWidth-trueWidth);
	});

}

//4.0 我要评价
function myComment(){
	//评价
	$(".btn_comment, .stable_comment").click(function(event){
		$(".commment_wrap").dialog({
			autoOpen: false,
			width: 250,
			height:170,
			modal: false,
			resizable: false,
			buttons: [
				{
					title: "取消",
					class: 'ser_keep',
					click: function() {
						$( this ).dialog( "close" );
					}
				},
				{
					title: "确定",
					class: 'ser_search',
					click: function() {
						$( this ).dialog( "close" );
					}
				}
			]	
		}).dialog("open");
	});		
}
	
/*
 *5.0 可编辑元素
 */
function editElement(element){
    var tds = $(element);  
    //给元素节点增加点击事件  
    tds.dblclick(tdclick);  
  
function tdclick(){  
    var td = $(this);  
    var text = td.text();  
    td.html("");  
    var input = $("<input type='text'>");  
  
    input.attr("value", text);  
    input.blur(function(event) {  
        var cfm = confirm("是否确认修改");  
          
        if(cfm){  
            var inputtext = $.trim(input.val());  
            var tdNode = input.parent();
            tdNode.html(inputtext);  
            tdNode.dblclick(tdclick);  
        }  
    });  
  
    //把文本框加到元素中去  
    td.append(input);
  
    //让文本狂中的文字被高亮选中  
    //需要将jquery的对象转换为dom对象  
    var inputdom = input.get(0);  
    inputdom.select();  
    //清除元素上注册的点击事件  
    td.unbind("click");  
}
	
}

/*6.0
 *侧边栏折叠
 */
function spSiderbar(){
	var folder=$(".folder"),
	    flag = true,
		leftWrap = $(".sp_left_wrap"),
		spLeft = $(".sp_left"), 
		folderShow = $(".folder_tips_hide"),
		folderList = $(".sp_content_m h3")//栏目;
		
	//侧边栏折叠	
	folder.click(function(){
		if(flag == true){
			leftWrap.hide();
			spLeft.css("width", 8);
			folder.css({"left:": 0});
			folderShow.css({"background-position": '-9px 0'});
			window.resizeWidth('.sub_content', 50);
		}else{
			leftWrap.show();
			spLeft.css("width", 235);
			//folder.css({"left:": 0});
			folderShow.css({"background-position": '0px 0'});
			window.resizeWidth('.sub_content', 300);
		}
		flag = !flag;
	});
	
	//栏目折叠
	folderList.click(function(){
		//console.log($(this))
		$(this).siblings(".siderbar_list").toggle();
		$(this).find("span").toggleClass("sp_tip");	
	});
	
	
}

/*7.0 操作提示
 *扩展jQuery对象本身
 *注：调用时需传递参数target，表示事件目标
 
jQuery.extend({
	qicTips: function(target){//操作提示方法
			var html ='<div class="operate_tips"><img src="images/operate.png"></div>';
			$("body").append(html);
			var operTips = $(".operate_tips");
			operTips.css({
				 "position": "absolute",
				 "top": $(target).offset().top -20,
				 "left": $(target).offset().left - 60,
				 "z-index": 90000,
				 'display': 'none'
			});
			operTips.stop(false, true).show('fast');
			window.setTimeout(function(){
				operTips.stop(false, true).hide('fast');
			}, 1000); 
	}
});*/

 /* 8.0
 *用户评级， 仿下拉框通用函数（用户评级）
 * clickWrap=当前点击ID, contentUlId=当前要显示隐藏的选项,hValue设置隐藏域data-value值， hName设置隐藏域data-name值
 *例:starChoose("select_info_year2", "#options_year2", "#reportDate2", "#reportName2");
 *
 */
function starChoose(clickWrap, contentUlId, hValue, hName){
	

$("body").click(function(event){
	var targetName = $(event.target).attr("id");
	var parentName = $(event.target).parent().attr("id");
	var optionsContent = $(contentUlId);
	var hiddeValue = $(hValue);
	var hiddeName = $(hName);
	if(targetName == clickWrap || parentName == clickWrap){
		$(event.target).parent().parent().find(".sel_91_option").slideToggle('fast');
	}else{
		

		$(contentUlId+" li").click(function(){
				var starClass = $(this).attr("class");
				console.log(starClass);
				var changeStar = $(this).parent().siblings('.sel_84_dialog').find("span");
				var dataValue = $(this).text();
				var dataName = $(this).attr("data-name");
				//changeStar.attr("class", 'star_3')
				switch(starClass){
					case 'star_5':
						changeStar.attr("class", 'star_5');
						hiddeValue.attr("data-value", dataValue)
						hiddeName.attr("data-name", dataName)
						break;
					case 'star_4':
						changeStar.attr("class", 'star_4');
						hiddeValue.attr("data-value", dataValue)
						hiddeName.attr("data-name", dataName)
						break;
					case 'star_3':
						changeStar.attr("class", 'star_3')
						hiddeValue.attr("data-value", dataValue)
						hiddeName.attr("data-name", dataName)
						break;
					case 'star_2':
						changeStar.attr("class", 'star_2')
						hiddeValue.attr("data-value", dataValue)
						hiddeName.attr("data-name", dataName)
						break;	
					case 'star_1':
						changeStar.attr("class", 'star_1')
						hiddeValue.attr("data-value", dataValue)
						hiddeName.attr("data-name", dataName)
						break;
					}
			
		
		});
		

		optionsContent.slideUp('fast');
	}
});

}


 /* 9.0
 *下拉框通用函数()
 * clickid=当前点击ID, downShow=当前要显示隐藏的选项, reportDate设置隐藏域reportDate值， reportName设置隐藏域reportName值
 *例:downBox('select_info_year1', '#options_year1', 'reportDate', 'reportName');

 *
 */
function downBox(clickId, downShow, hiddenValueId, hiddenNameId){
	$('body').click(function(event){
		var cID = '#' + clickId; //给ID加一个#,以便JQ选中
		var dList = downShow + ' '+'li'; //点击选项列表
		
		if(event.target.id == clickId){
			//控制再一次点击 当前显示项关闭
		 	$(downShow).stop(false,true).slideUp("fast");
			$(""+downShow+":hidden").stop(false,true).slideDown("fast");
			
			  $(dList).click(function(){
				  $(cID).text(''); //清空点击ID的值
				  var txt = $(this).text();//获得当前点击列表项的值
				  
				  var hiddenValue = $(this).attr("data-value");
				  
					  
				   $("#reportDate").val(hiddenValue)
				  
				  if($(this).is(".open_selected")){
					  $(cID).html(txt).css("color","#7D7D7D"); //设置选择的值
					  
					  $("#" + hiddenValueId).val(hiddenValue);
					  $("#" + hiddenNameId).val(txt);
					  
				  }else{
					  $(cID).html(txt).css("color","#000"); //设置选择的值
					  
					   $("#" + hiddenValueId).val(hiddenValue);
					  $("#" + hiddenNameId).val(txt);
					  //$('#options_certificate').css("display", "none"); 
					  //eventstopPropagation();
				  }
			  });
				  
		 }else{
			 //downShow.slideUp();
			 $(downShow).slideUp("fast").hide(); //隐藏下拉框
		 }

	});
}

//10.0
//tab 选项卡添加、左右移动
var controlUtil = function(){
		var controlElement = $('.tab_maximum');
		//var controlList = controlElement.children("li");
		
		
		return {
			//遍历tab选项卡, 返回整个选项卡的left
			eachList: function(){
				
				var leftList = []; 
				var currentItemLeft = 0;
				controlElement.children("li").each(function(i){
					if(i>0){
						currentItemLeft = parseInt(leftList[i-1]) + $(this).prev().width() + 7 +30;
					}
					leftList.push(currentItemLeft);
				
				});
				return leftList;
			},
			
			//移到上一个tab选项
			prevTabItem: function(){
				
				var leftList = [];//记录每个tab选项的left, 由左到右
				var	sumwidth = 0,
					currentItemLeft= 0,
				    tabItems = controlElement.children("li");
				
				tabItems.each(function(){
					sumwidth += $(this).width() + 7 + 30;
				});	
				
				controlElement.children("li").each(function(i){
					if(i>0){
						currentItemLeft = parseInt(leftList[i-1]) + $(this).prev().width() + 7 +30;
					}
					leftList.push(currentItemLeft);
				
				});
				
				var currentLeft = -parseInt(controlElement.css("left")); //当前位置	
				
				for(var j=0; j<leftList.length; j++){
					if(currentLeft>=leftList[j-1] && currentLeft<leftList[j]){
						console.log(j  , leftList[j]);
						controlElement.animate({left: -parseInt(leftList[j])});
						return false;
					}
				}
					
				
//				//总宽
//				tabItems.each(function(){
//					sumwidth += $(this).width() + 7 + 30;
//				});	
//				
//				var mainwidth = $(".tab_title").width();
//				
//				for(var i=tabItems.length -1; i>=0; i--){
//					var currentItemLeft = sumwidth - mainwidth;
//					if(i != tabItems.length - 1){
//						//currentItemLeft = leftList[tabItems.length - 2 - i]- $(tabItems[i + 1]).width();
//						currentItemLeft = parseInt(leftList[i-1]) + $(this).prev().width() + 7 +30;
//						
//						
//					}
//					leftList.push(currentItemLeft);
//				}
//				console.log(leftList); 
//				
//				var currentLeft = -1 * parseInt(controlElement.css("left"));
//				//console.log(currentLeft)
//				for (var j = 1; j < leftList.length; j++)
//				{
//					if (leftList[j] <= currentLeft && leftList[j - 1] > currentLeft)
//					{
//						//controlElement.animate({ left: -1 * parseInt(leftList[j - 1]) });
//						controlElement.animate({ left: -1 * parseInt(leftList[j - 1]) });
//						return;
//					}
//				}
				
				
// var g = this, p = this.options;
//            var btnWitdth = $(".l-tab-links-right", g.tab).width();
//            var sumwidth = 0;
//            var tabItems = $("li", g.tab.links.ul);
//            tabItems.each(function ()
//            {
//                sumwidth += $(this).width() + 2;
//            });
//            var mainwidth = g.tab.width();
//            var leftList = new Array(); //记录每个tab的left,由右到左 
//            for (var i = tabItems.length - 1; i >= 0; i--)
//            {
//                var currentItemLeft = sumwidth - mainwidth + btnWitdth + 2;
//                if (i != tabItems.length - 1)
//                {
//                    currentItemLeft = parseInt(leftList[tabItems.length - 2 - i]) - $(tabItems[i + 1]).width() - 2;
//                }
//                leftList.push(currentItemLeft);
//            }
				
			
			},
			
			//下一个tab选项卡
			nextTabItem: function(){
				//记录每个tab的left,由右到左 
				var leftList = controlUtil.eachList();
				console.log(leftList)
				var currentLeft =  -parseInt(controlElement.css("left"));
				for(var i=0; i<leftList.length; i++){
					//if(currentLeft<=leftList[i] && currentLeft > leftList[i-1]){
					if(leftList[i]>currentLeft && leftList[i-1]<=currentLeft){	
						controlElement.animate({left: -1*parseInt(leftList[i])});
						return false;
					}
					
				}
			},
			
			//增加一个tab
			addTabItem: function(){
				
			}
		};
		
}();
