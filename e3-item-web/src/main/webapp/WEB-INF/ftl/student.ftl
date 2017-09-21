<html>
<head>
	<title>student</title>
</head>
<body>
	<table border="1">
		<tr>
			<th>序号</th>
			<th>学号</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>地址</th>
		</tr>
		<#list studentList as stu>
		<#if stu_index % 2 == 0>
			<tr bgcolor="red">
		<#else>
			<tr bgcolor="green">
		</#if>
		<tr>
			<th>${stu_index}</th>
			<th>${stu.id}</th>
			<th>${stu.name}</th>
			<th>${stu.age}</th>
			<th>${stu.address}</th>
		</tr>
		</#list>
	</table>
	<br>
	当前时间:${date?string("yyyy/MM/dd hh:mm:ss")}
	<br>
	null值的处理：${val!"val的值为null"}
	<br>
	引用模板测试
	<#include "hello.ftl">
</body>
</html>