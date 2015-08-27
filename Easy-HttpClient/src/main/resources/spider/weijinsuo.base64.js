function wbase64(a) {
	var b, c, d, e, f, g = "", h = "", i = "", j = 0, k = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
	do
		b = a.charCodeAt(j++), c = a.charCodeAt(j++), h = a.charCodeAt(j++),
				d = b >> 2, e = (3 & b) << 4 | c >> 4, f = (15 & c) << 2
						| h >> 6, i = 63 & h, isNaN(c) ? f = i = 64 : isNaN(h)
						&& (i = 64), g = g + k.charAt(d) + k.charAt(e)
						+ k.charAt(f) + k.charAt(i), b = c = h = "",
				d = e = f = i = "";
	while (j < a.length);
	return g
}