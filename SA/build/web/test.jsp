<%-- 
    Document   : test
    Created on : 2014/3/30, 下午 10:40:02
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head> 
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" /> 
    <title></title> 
    <script type="text/javascript"> 
         function checkdate() {    
            var s1 = document.getElementById("txtstart").value; 
            var s2 = document.getElementById("txtend").value;
            var s3 = document.getElementById("txtstart1").value; 
            var s4 = document.getElementById("txtend1").value; 

            var TRsDate = new Date(s1); 
            var TReDate = new Date(s2);
            var TEsDate = new Date(s3); 
            var TEeDate = new Date(s4); 
            //alert(sDate + "=============" + eDate); 
            alert(s1)
            alert(TRsDate > TReDate)
            if((TRsDate > TReDate) || (TEsDate > TEeDate))
            { 
             alert("資料的结束日期不能小于开始日期");
             return false; 
             }
            return true; 
       } 
    </script> 
</head> 
<body> 
    <form action="cor.jsp" onsubmit="return checkdate()"> 
    <input type="date" value="2012-03-13" id="txtstart"/> 
    <input type="date" value="2012-03-12" id="txtend"/>
    <input type="date" value="2012-03-13" id="txtstart1"/> 
    <input type="date" value="2012-03-12" id="txtend1"/>
    <input type="submit" value="submit"/>
    </form>
</body>
</html>
