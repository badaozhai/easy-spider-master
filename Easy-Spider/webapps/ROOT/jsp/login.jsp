<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link  href="css/user.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
</script>
<title>登录__</title>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
</head>
<body>
<!--顶部-->
<div id="top">
    <div class="layout">
        <!-- <div class="fl"><a href="/mobile.html" target="_blank" rel="nofollow"><i></i>手机草根理财</a></div> -->
        <ul class="fr" id="topheader">
            <li><a href="/faq.html" target="_blank">帮助中心</a><span>|</span></li>
            <li>
                <a href="/aboutus.html" target="_blank">关于我们</a><span>|</span>
            </li>
                        <li><a href="/login.html" target="_blank">登录</a><span>|</span></li>
                    </ul>
    </div>
</div>
<!--顶部 end-->
<!--头部-->
<div id="header">
    <div class="layout">
        <div id="logo"><a href="index.html">
                <!-- <img src="https://cgtzfiles.b0.upaiyun.com/style3/css/img/logo.png" alt="草根个人理财网" title="草根优选理财产品项目，让投资实现梦想" /> --></a>
            </div>
        </div>
</div>
<div class="height18"></div>
<div class="height18"></div>
<div class="layoutUser">
    <div class="borderGray bgWhite clearfix" id="login">
        <div class="bannerImg"><img src="image/loginimg.jpg" alt="" /></div>
        <div class="loginRight">
            <div class="title">用户登录</div>
            <div class="height10"></div>
            <div class="height10"></div>
            <form autocomplete="off" id="login-form" action="/login" method="post">
<div style="display:none"><input type="hidden" value="5573f57e7c83f1771332bc9eade905372715b060" name="token" /></div>                <div class="loginError" style="height:30px;overflow:hidden;display:none" id="login-form_es_">
<ul><li>dummy</li></ul></div>                <ul class="formUl">
                    <li class="mobileLi">
                        <label class="name">账&nbsp;&nbsp;&nbsp;&nbsp;号</label>
                        <input class="textInput" placeholder="手机号" name="username" id="LoginForm_username" type="text" maxlength="100" />                        <i class="iA"></i>
                        <label id="User_mobileTips"></label>
                    </li>
                    <li class="passwordLi">
                        <label class="name">密&nbsp;&nbsp;&nbsp;&nbsp;码</label>
                        <input class="textInput" maxlength="20" name="password" id="LoginForm_password" type="password" />                        <i></i>
                        <label id="User_passwordTips"></label>
                    </li>
                                        <li class="goLi"><!-- <a href="/repass_step_first.html" class="aGreen" target="_blank">忘记登录密码？</a> --></li>
                    <li class="bntLi"><input type="submit" class="" id="submit" value="立即登录" /></li>
                </ul>
                <input type="hidden" value="" name="from" id="from" />            </form> 
        </div>
    </div>
</div>

</body>
</html>
