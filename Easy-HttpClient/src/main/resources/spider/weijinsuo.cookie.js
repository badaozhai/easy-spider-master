var n= /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
 var cookies = "";
function cookie(a)
{
    cookie(a, ss, ss);
};
function cookie(a, b)
{
    cookie(a, b, ss);
};
function trim(a) {
   return null == a ? "" : (a + "").replace(n, "")
};
function cookie(a, b, c)
{
    if ("undefined" == typeof b)
    {
        var d = null;
        if (cookies && "" != cookies)
            for (var e = cookies.split(";"), f = 0; f < e.length; f++)
            {
                var g = trim(e[f]);
                if (g.substring(0, a.length + 1) == a + "=")
                {
                    d = decodeURIComponent(g.substring(a.length + 1));
                    break
                }
            }
        return d
    }
    c = c || {},
    null === b && (b = "", c.expires = -1);
    var h = "";
    if (c.expires && ("number" == typeof c.expires || c.expires.toUTCString))
    {
        var i;
        "number" == typeof c.expires ? (i = new Date, i.setTime(i.getTime() + 24 * c.expires * 60 * 60 * 1e3)) : i = c.expires,
        h = "; expires=" + i.toUTCString()
    }
    var j = c.path ? "; path=" + c.path : "; path=/",
    k = c.domain ? "; domain=" + c.domain : "",
    l = c.secure ? "; secure" : "";
    cookies =cookies+";"+ [a, "=", encodeURIComponent(b), h, j, k, l].join("");
    return cookies;
};
function logCookie(b, c)
{
	
	if(b instanceof Object){
		
	}else{
		b=JSON.parse(b);
	}
	
	
    if (!b)
    {
        var d = cookie("ul");
        return d ? d.split("&") : null
    }
    var d = b,
    e = d.userSession,
    f = [d.userId, d.username, d.phone, d.email, d.userLevel, d.authLevel];
    f.push(handleTime()),
    f.push(d.headPic);
    var g = [];
//    d.permissionList && d.permissionList.length && a.each(d.permissionList, function (a, b)
//    {
        g.push(b.code)
//    }
//    ),
    f.push(g.join(",")),
    f.push(d.loanStatus),
    f.push(d.webFirstLogin),
    c && cookie("u", e,''),
    cookie("ul", f.join("&"),
    {
        expires : 15
    }
    ),
    cookie("um", d.phone,
    {
        expires : 15
    }
    )
    return cookies;
};

function handleTime()
{
    var a = (new Date).getTime();
    return a.toString(36)
};

