$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	// 点击发布就隐藏弹出的框[让填写标题和正文的]
	$("#publishModal").modal("hide");
	// 获取标题和内容 发送异步请求
	var title=$("#recipient-name").val();
	var content=$("#message-text").val();
	$.post(
		CONTEXT_PATH+"/discuss/add",
		{"title":title,"content":content},
		function (data) {
			//将字符串解析了js对象
			data=$.parseJSON(data);
			//在提示框中返回信息
			$("#modal-body").text(data.msg);
			//显示提示框
			$("#hintModal").modal("show");
			//2秒后自动隐藏
			setTimeout(function(){
				$("#hintModal").modal("hide");
				//刷新页面 发布成功时才刷新
				if (data.code=0){
					window.location.reload();
				}
			}, 2000);
		}
	)
}
