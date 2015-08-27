# easy-httpclient
这是我封装的httpclient，基于httpclient4.3.2.<br>
1.处理网页编码：<br> 
&nbsp;&nbsp;先判断相应头，如果相应头中content-type有网页编码，那么编码使用网页编码，<br>
&nbsp;&nbsp;如果没有，那么会在网页中通过jsoup获得网页头里的charset，如果在没有找到，使用默认编码utf-8。
2.post请求：<br>
&nbsp;&nbsp;针对一般的post请求，直接设置header，postdata即可。<br>
&nbsp;&nbsp;针对不是表单提交的post请求<br>
&nbsp;&nbsp;* 如果想提交一段字符串<br>
&nbsp;&nbsp;* 那么需要将header中的content-type设置成非application/x-www-form-urlencoded;<br>
&nbsp;&nbsp;* 将字符串放到postdata中参数名postdata<br>
