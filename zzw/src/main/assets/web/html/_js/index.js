
var usnObj;
var pswObj;

var hintObj;

// 初始化函数, 页面加载完毕的时候回调
function init(){
	
	usnObj = document.getElementById("usn");
	pswObj = document.getElementById("psw");
	
	hintObj = document.getElementById("hint");

}

// 登录的时候表单验证
function onLogin(){
	var usn = usnObj.value;
	var psw = pswObj.value;
	
	if(usn == ""){
		hintObj.innerHTML="请输入用户名";
		return false;
	}
	
	if(psw == ""){
		hintObj.innerHTML="请输入密码";
		return false;
	}
}