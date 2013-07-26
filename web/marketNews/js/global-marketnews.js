/* 
 *func: NT市场资讯
 *author:jinwei
 *email: awayInner@gmail.com
 *date: 2012 january 14
 */ 

/*1.0
 *左边子菜单显示隐藏
 */


(function(){
	//折叠
	window.addEventListener("load", function(){
		function selector(name){
			return document.querySelectorAll(name);	
		};
		
		var folder = $("div.folder");
			siderNews = $(".sider_news"),
			barWidth = siderNews.width(),
			mainWrap = $(".main_wrap");
			wrapLeft = mainWrap.css("margin-left"),
			folderSc = $(".folder"),
			sLeft = folderSc.css("left");
			
		//点击
		folder.click(function(){
			var _thisClass = this.children[0].className,
				slideNews = selector(".sider_news")[0],
				main_wrap = selector(".main_wrap")[0],
				folderShow = selector(".folder_tips_hide")[0],
				hideTips = 'folder_tips_hide',
				showTips = 'folder_tips_show';
			
			
			if(_thisClass == hideTips){
				mainWrap.animate({"margin-left": 30});
				siderNews.animate({"width": 0, "overflow": "hidden"});
				this.children[0].className = showTips;
				siderNews.animate({"overflow": "hidden"});
				folderSc.animate({"left": 0});
			}else{
				mainWrap.animate({"margin-left": wrapLeft});
				siderNews.stop(false, true).animate({"width": barWidth});
				this.children[0].className = hideTips;
				folderSc.stop(false, true).animate({"left": sLeft});
			}
			
		});	
	}, false)
})();



//2.0市场资讯
var marketNews = {
		//2.1客户端高度实时变化
		contentResize: function(selector, redHigh){
			//内容高度
			var clientHeight = document.documentElement.clientHeight,
				detail = document.querySelector(selector);
				detail.style.height =  clientHeight - redHigh + "px"; 
			
			window.onresize = function(){
				var clientHeight = document.documentElement.clientHeight;
				detail.style.height =  clientHeight - redHigh + "px"; 
			}
	
		},
		
		//2.2我的订阅、高级搜索
		mySubscribe: function(){
			$("body").click(function(e){
					var target = e.target;
					var className = target.className,
						aClassName;
					//提取class	
					if( /(sel_subscribe)/.test(className) ){
						aClassName = RegExp['$1']
					}
					
					if(aClassName == 'sel_subscribe'){
						$(target).siblings(".sel_subscribe_opt").slideToggle(100);	
					}else{
						$(".sel_subscribe_opt").slideUp(100);
					}
			});
			
		},
		
		//2.3动态加载样式
		loadStyleString: function(){
				var style = document.createElement("style");
					style.type = "text/css";
				var string = document.createTextNode(css);
				try {
					style.appendChild(string);
				}catch(ex){
					style.styleSheet.cssText = css;
				}	
				var head = document.getElementsByTagName("head")[0];
				head.appendChild(style);
			
		},
		
		//2.4子菜单
		subMenu: function(){
			$(".for_folder").toggle(function(){
				$(this).find(".sp_tip").toggleClass("sp_tip_2");
				$(this).siblings(".submenu_mn").slideToggle()

			}, function(){
				$(this).find(".sp_tip").toggleClass("sp_tip_2");
				$(this).siblings(".submenu_mn").slideToggle();
			});
			
		},
		
		//2.5模仿select
		downBox: function(clickId, downShow, hiddenValueId, hiddenNameId){
		
			//function downBox(clickId, downShow, hiddenValueId, hiddenNameId){
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
							  }
						  });
							  
					 }else{
						 //downShow.slideUp();
						 $(downShow).slideUp("fast").hide(); //隐藏下拉框
					 }
			
				});
				
		 },
		 
		 //2.6 jQ tab切换
		 tabClick: function(){
			$(".tab_li li").click(function(){
				var index = $(this).index();
				$(".tab_li").children("li").removeClass().eq(index).addClass('display');
				$(".sub_content").children("li").removeClass().eq(index).addClass('display')
			});
			 
		 }
		
	
};



