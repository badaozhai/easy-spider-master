function BigInteger(e, t, n) {
    e != null && ("number" == typeof e ? this.fromNumber(e, t, n) : t == null && "string" != typeof e ? this.fromString(e, 256) : this.fromString(e, t))
}
function nbi() {
    return new BigInteger(null)
}
function am1(e, t, n, r, i, s) {
    while (--s >= 0) {
        var o = t * this[e++] + n[r] + i;
        i = Math.floor(o / 67108864), n[r++] = o & 67108863
    }
    return i
}
function am2(e, t, n, r, i, s) {
    var o = t & 32767, u = t >> 15;
    while (--s >= 0) {
        var a = this[e] & 32767, f = this[e++] >> 15, l = u * a + f * o;
        a = o * a + ((l & 32767) << 15) + n[r] + (i & 1073741823), i = (a >>> 30) + (l >>> 15) + u * f + (i >>> 30), n[r++] = a & 1073741823
    }
    return i
}
function am3(e, t, n, r, i, s) {
    var o = t & 16383, u = t >> 14;
    while (--s >= 0) {
        var a = this[e] & 16383, f = this[e++] >> 14, l = u * a + f * o;
        a = o * a + ((l & 16383) << 14) + n[r] + i, i = (a >> 28) + (l >> 14) + u * f, n[r++] = a & 268435455
    }
    return i
}
function int2char(e) {
    return BI_RM.charAt(e)
}
function intAt(e, t) {
    var n = BI_RC[e.charCodeAt(t)];
    return n == null ? -1 : n
}
function bnpCopyTo(e) {
    for (var t = this.t - 1; t >= 0; --t)
        e[t] = this[t];
    e.t = this.t, e.s = this.s
}
function bnpFromInt(e) {
    this.t = 1, this.s = e < 0 ? -1 : 0, e > 0 ? this[0] = e : e < -1 ? this[0] = e + DV : this.t = 0
}
function nbv(e) {
    var t = nbi();
    return t.fromInt(e), t
}
function bnpFromString(e, t) {
    var n;
    if (t == 16)
        n = 4;
    else if (t == 8)
        n = 3;
    else if (t == 256)
        n = 8;
    else if (t == 2)
        n = 1;
    else if (t == 32)
        n = 5;
    else {
        if (t != 4) {
            this.fromRadix(e, t);
            return
        }
        n = 2
    }
    this.t = 0, this.s = 0;
    var r = e.length, i = !1, s = 0;
    while (--r >= 0) {
        var o = n == 8 ? e[r] & 255 : intAt(e, r);
        if (o < 0) {
            e.charAt(r) == "-" && (i = !0);
            continue
        }
        i = !1, s == 0 ? this[this.t++] = o : s + n > this.DB ? (this[this.t - 1] |= (o & (1 << this.DB - s) - 1) << s, this[this.t++] = o >> this.DB - s) : this[this.t - 1] |= o << s, s += n, s >= this.DB && (s -= this.DB)
    }
    n == 8 && (e[0] & 128) != 0 && (this.s = -1, s > 0 && (this[this.t - 1] |= (1 << this.DB - s) - 1 << s)), this.clamp(), i && BigInteger.ZERO.subTo(this, this)
}
function bnpClamp() {
    var e = this.s & this.DM;
    while (this.t > 0 && this[this.t - 1] == e)
        --this.t
}
function bnToString(e) {
    if (this.s < 0)
        return "-" + this.negate().toString(e);
    var t;
    if (e == 16)
        t = 4;
    else if (e == 8)
        t = 3;
    else if (e == 2)
        t = 1;
    else if (e == 32)
        t = 5;
    else {
        if (e != 4)
            return this.toRadix(e);
        t = 2
    }
    var n = (1 << t) - 1, r, i = !1, s = "", o = this.t, u = this.DB - o * this.DB % t;
    if (o-- > 0) {
        u < this.DB && (r = this[o] >> u) > 0 && (i = !0, s = int2char(r));
        while (o >= 0)
            u < t ? (r = (this[o] & (1 << u) - 1) << t - u, r |= this[--o] >> (u += this.DB - t)) : (r = this[o] >> (u -= t) & n, u <= 0 && (u += this.DB, --o)), r > 0 && (i = !0), i && (s += int2char(r))
    }
    return i ? s : "0"
}
function bnNegate() {
    var e = nbi();
    return BigInteger.ZERO.subTo(this, e), e
}
function bnAbs() {
    return this.s < 0 ? this.negate() : this
}
function bnCompareTo(e) {
    var t = this.s - e.s;
    if (t != 0)
        return t;
    var n = this.t;
    t = n - e.t;
    if (t != 0)
        return t;
    while (--n >= 0)
        if ((t = this[n] - e[n]) != 0)
            return t;
    return 0
}
function nbits(e) {
    var t = 1, n;
    return (n = e >>> 16) != 0 && (e = n, t += 16), (n = e >> 8) != 0 && (e = n, t += 8), (n = e >> 4) != 0 && (e = n, t += 4), (n = e >> 2) != 0 && (e = n, t += 2), (n = e >> 1) != 0 && (e = n, t += 1), t
}
function bnBitLength() {
    return this.t <= 0 ? 0 : this.DB * (this.t - 1) + nbits(this[this.t - 1] ^ this.s & this.DM)
}
function bnpDLShiftTo(e, t) {
    var n;
    for (n = this.t - 1; n >= 0; --n)
        t[n + e] = this[n];
    for (n = e - 1; n >= 0; --n)
        t[n] = 0;
    t.t = this.t + e, t.s = this.s
}
function bnpDRShiftTo(e, t) {
    for (var n = e; n < this.t; ++n)
        t[n - e] = this[n];
    t.t = Math.max(this.t - e, 0), t.s = this.s
}
function bnpLShiftTo(e, t) {
    var n = e % this.DB, r = this.DB - n, i = (1 << r) - 1, s = Math.floor(e / this.DB), o = this.s << n & this.DM, u;
    for (u = this.t - 1; u >= 0; --u)
        t[u + s + 1] = this[u] >> r | o, o = (this[u] & i) << n;
    for (u = s - 1; u >= 0; --u)
        t[u] = 0;
    t[s] = o, t.t = this.t + s + 1, t.s = this.s, t.clamp()
}
function bnpRShiftTo(e, t) {
    t.s = this.s;
    var n = Math.floor(e / this.DB);
    if (n >= this.t) {
        t.t = 0;
        return
    }
    var r = e % this.DB, i = this.DB - r, s = (1 << r) - 1;
    t[0] = this[n] >> r;
    for (var o = n + 1; o < this.t; ++o)
        t[o - n - 1] |= (this[o] & s) << i, t[o - n] = this[o] >> r;
    r > 0 && (t[this.t - n - 1] |= (this.s & s) << i), t.t = this.t - n, t.clamp()
}
function bnpSubTo(e, t) {
    var n = 0, r = 0, i = Math.min(e.t, this.t);
    while (n < i)
        r += this[n] - e[n], t[n++] = r & this.DM, r >>= this.DB;
    if (e.t < this.t) {
        r -= e.s;
        while (n < this.t)
            r += this[n], t[n++] = r & this.DM, r >>= this.DB;
        r += this.s
    } else {
        r += this.s;
        while (n < e.t)
            r -= e[n], t[n++] = r & this.DM, r >>= this.DB;
        r -= e.s
    }
    t.s = r < 0 ? -1 : 0, r < -1 ? t[n++] = this.DV + r : r > 0 && (t[n++] = r), t.t = n, t.clamp()
}
function bnpMultiplyTo(e, t) {
    var n = this.abs(), r = e.abs(), i = n.t;
    t.t = i + r.t;
    while (--i >= 0)
        t[i] = 0;
    for (i = 0; i < r.t; ++i)
        t[i + n.t] = n.am(0, r[i], t, i, 0, n.t);
    t.s = 0, t.clamp(), this.s != e.s && BigInteger.ZERO.subTo(t, t)
}
function bnpSquareTo(e) {
    var t = this.abs(), n = e.t = 2 * t.t;
    while (--n >= 0)
        e[n] = 0;
    for (n = 0; n < t.t - 1; ++n) {
        var r = t.am(n, t[n], e, 2 * n, 0, 1);
        (e[n + t.t] += t.am(n + 1, 2 * t[n], e, 2 * n + 1, r, t.t - n - 1)) >= t.DV && (e[n + t.t] -= t.DV, e[n + t.t + 1] = 1)
    }
    e.t > 0 && (e[e.t - 1] += t.am(n, t[n], e, 2 * n, 0, 1)), e.s = 0, e.clamp()
}
function bnpDivRemTo(e, t, n) {
    var r = e.abs();
    if (r.t <= 0)
        return;
    var i = this.abs();
    if (i.t < r.t) {
        t != null && t.fromInt(0), n != null && this.copyTo(n);
        return
    }
    n == null && (n = nbi());
    var s = nbi(), o = this.s, u = e.s, a = this.DB - nbits(r[r.t - 1]);
    a > 0 ? (r.lShiftTo(a, s), i.lShiftTo(a, n)) : (r.copyTo(s), i.copyTo(n));
    var f = s.t, l = s[f - 1];
    if (l == 0)
        return;
    var c = l * (1 << this.F1) + (f > 1 ? s[f - 2] >> this.F2 : 0), h = this.FV / c, p = (1 << this.F1) / c, d = 1 << this.F2, v = n.t, m = v - f, g = t == null ? nbi() : t;
    s.dlShiftTo(m, g), n.compareTo(g) >= 0 && (n[n.t++] = 1, n.subTo(g, n)), BigInteger.ONE.dlShiftTo(f, g), g.subTo(s, s);
    while (s.t < f)
        s[s.t++] = 0;
    while (--m >= 0) {
        var y = n[--v] == l ? this.DM : Math.floor(n[v] * h + (n[v - 1] + d) * p);
        if ((n[v] += s.am(0, y, n, m, 0, f)) < y) {
            s.dlShiftTo(m, g), n.subTo(g, n);
            while (n[v] < --y)
                n.subTo(g, n)
        }
    }
    t != null && (n.drShiftTo(f, t), o != u && BigInteger.ZERO.subTo(t, t)), n.t = f, n.clamp(), a > 0 && n.rShiftTo(a, n), o < 0 && BigInteger.ZERO.subTo(n, n)
}
function bnMod(e) {
    var t = nbi();
    return this.abs().divRemTo(e, null, t), this.s < 0 && t.compareTo(BigInteger.ZERO) > 0 && e.subTo(t, t), t
}
function Classic(e) {
    this.m = e
}
function cConvert(e) {
    return e.s < 0 || e.compareTo(this.m) >= 0 ? e.mod(this.m) : e
}
function cRevert(e) {
    return e
}
function cReduce(e) {
    e.divRemTo(this.m, null, e)
}
function cMulTo(e, t, n) {
    e.multiplyTo(t, n), this.reduce(n)
}
function cSqrTo(e, t) {
    e.squareTo(t), this.reduce(t)
}
function bnpInvDigit() {
    if (this.t < 1)
        return 0;
    var e = this[0];
    if ((e & 1) == 0)
        return 0;
    var t = e & 3;
    return t = t * (2 - (e & 15) * t) & 15, t = t * (2 - (e & 255) * t) & 255, t = t * (2 - ((e & 65535) * t & 65535)) & 65535, t = t * (2 - e * t % this.DV) % this.DV, t > 0 ? this.DV - t : -t
}
function Montgomery(e) {
    this.m = e, this.mp = e.invDigit(), this.mpl = this.mp & 32767, this.mph = this.mp >> 15, this.um = (1 << e.DB - 15) - 1, this.mt2 = 2 * e.t
}
function montConvert(e) {
    var t = nbi();
    return e.abs().dlShiftTo(this.m.t, t), t.divRemTo(this.m, null, t), e.s < 0 && t.compareTo(BigInteger.ZERO) > 0 && this.m.subTo(t, t), t
}
function montRevert(e) {
    var t = nbi();
    return e.copyTo(t), this.reduce(t), t
}
function montReduce(e) {
    while (e.t <= this.mt2)
        e[e.t++] = 0;
    for (var t = 0; t < this.m.t; ++t) {
        var n = e[t] & 32767, r = n * this.mpl + ((n * this.mph + (e[t] >> 15) * this.mpl & this.um) << 15) & e.DM;
        n = t + this.m.t, e[n] += this.m.am(0, r, e, t, 0, this.m.t);
        while (e[n] >= e.DV)
            e[n] -= e.DV, e[++n]++
    }
    e.clamp(), e.drShiftTo(this.m.t, e), e.compareTo(this.m) >= 0 && e.subTo(this.m, e)
}
function montSqrTo(e, t) {
    e.squareTo(t), this.reduce(t)
}
function montMulTo(e, t, n) {
    e.multiplyTo(t, n), this.reduce(n)
}
function bnpIsEven() {
    return (this.t > 0 ? this[0] & 1 : this.s) == 0
}
function bnpExp(e, t) {
    if (e > 4294967295 || e < 1)
        return BigInteger.ONE;
    var n = nbi(), r = nbi(), i = t.convert(this), s = nbits(e) - 1;
    i.copyTo(n);
    while (--s >= 0) {
        t.sqrTo(n, r);
        if ((e & 1 << s) > 0)
            t.mulTo(r, i, n);
        else {
            var o = n;
            n = r, r = o
        }
    }
    return t.revert(n)
}
function bnModPowInt(e, t) {
    var n;
    return e < 256 || t.isEven() ? n = new Classic(t) : n = new Montgomery(t), this.exp(e, n)
}
function Arcfour() {
    this.i = 0, this.j = 0, this.S = new Array
}
function ARC4init(e) {
    var t, n, r;
    for (t = 0; t < 256; ++t)
        this.S[t] = t;
    n = 0;
    for (t = 0; t < 256; ++t)
        n = n + this.S[t] + e[t % e.length] & 255, r = this.S[t], this.S[t] = this.S[n], this.S[n] = r;
    this.i = 0, this.j = 0
}
function ARC4next() {
    var e;
    return this.i = this.i + 1 & 255, this.j = this.j + this.S[this.i] & 255, e = this.S[this.i], this.S[this.i] = this.S[this.j], this.S[this.j] = e, this.S[e + this.S[this.i] & 255]
}
function prng_newstate() {
    return new Arcfour
}
function rng_seed_int(e) {
    rng_pool[rng_pptr++] ^= e & 255, rng_pool[rng_pptr++] ^= e >> 8 & 255, rng_pool[rng_pptr++] ^= e >> 16 & 255, rng_pool[rng_pptr++] ^= e >> 24 & 255, rng_pptr >= rng_psize && (rng_pptr -= rng_psize)
}
function rng_seed_time() {
    rng_seed_int((new Date).getTime())
}
function rng_get_byte() {
    if (rng_state == null) {
        rng_seed_time(), rng_state = prng_newstate(), rng_state.init(rng_pool);
        for (rng_pptr = 0; rng_pptr < rng_pool.length; ++rng_pptr)
            rng_pool[rng_pptr] = 0;
        rng_pptr = 0
    }
    return rng_state.next()
}
function rng_get_bytes(e) {
    var t;
    for (t = 0; t < e.length; ++t)
        e[t] = rng_get_byte()
}
function SecureRandom() {
}
function parseBigInt(e, t) {
    return new BigInteger(e, t)
}
function linebrk(e, t) {
    var n = "", r = 0;
    while (r + t < e.length)
        n += e.substring(r, r + t) + "\n", r += t;
    return n + e.substring(r, e.length)
}
function byte2Hex(e) {
    return e < 16 ? "0" + e.toString(16) : e.toString(16)
}
function pkcs1pad2(e, t) {
    if (t < e.length + 2)
        return alert("密码太长!"), null;
    var n = new Array, r = e.length - 1, i = e.length;
    if (i < 100) {
        n[0] = 48 + i / 10, n[1] = 48 + i % 10;
        var s = 2;
        r = 0;
        while (r < i && t > 0)
            n[s++] = e.charCodeAt(r++);
        var o = new SecureRandom, u = new Array;
        while (s < t) {
            u[0] = 0;
            while (u[0] == 0)
                o.nextBytes(u);
            n[s++] = u[0]
        }
        return new BigInteger(n)
    }
    return alert("密码太长!"), null
}
function RSAKey() {
    this.n = null, this.e = 0, this.d = null, this.p = null, this.q = null, this.dmp1 = null, this.dmq1 = null, this.coeff = null
}
function RSASetPublic(e, t) {
    e != null && t != null && e.length > 0 && t.length > 0 ? (this.n = parseBigInt(e, 16), this.e = parseInt(t, 16)) : alert("Invalid RSA public key")
}
function RSADoPublic(e) {
    return e.modPowInt(this.e, this.n)
}
function RSAEncrypt(e) {
    var t = pkcs1pad2(e, this.n.bitLength() + 7 >> 3);
    if (t == null)
        return null;
    var n = this.doPublic(t);
    if (n == null)
        return null;
    var r = n.toString(16).toUpperCase(), i = 256 - r.length;
    for (var s = 0; s < i; s += 1)
        r = "0" + r;
    return r
}
function hex2b64(e) {
    var t, n, r = "";
    for (t = 0; t + 3 <= e.length; t += 3)
        n = parseInt(e.substring(t, t + 3), 16), r += b64map.charAt(n >> 6) + b64map.charAt(n & 63);
    t + 1 == e.length ? (n = parseInt(e.substring(t, t + 1), 16), r += b64map.charAt(n << 2)) : t + 2 == e.length && (n = parseInt(e.substring(t, t + 2), 16), r += b64map.charAt(n >> 2) + b64map.charAt((n & 3) << 4));
    while ((r.length & 3) > 0)
        r += b64pad;
    return r
}
function b64tohex(e) {
    var t = "", n, r = 0, i;
    for (n = 0; n < e.length; ++n) {
        if (e.charAt(n) == b64pad)
            break;
        v = b64map.indexOf(e.charAt(n));
        if (v < 0)
            continue;
        r == 0 ? (t += int2char(v >> 2), i = v & 3, r = 1) : r == 1 ? (t += int2char(i << 2 | v >> 4), i = v & 15, r = 2) : r == 2 ? (t += int2char(i), t += int2char(v >> 2), i = v & 3, r = 3) : (t += int2char(i << 2 | v >> 4), t += int2char(v & 15), r = 0)
    }
    return r == 1 && (t += int2char(i << 2)), t
}
function b64toBA(e) {
    var t = b64tohex(e), n, r = new Array;
    for (n = 0; 2 * n < t.length; ++n)
        r[n] = parseInt(t.substring(2 * n, 2 * n + 2), 16);
    return r
}
function liuhjtest() {
    var e = "B1204FC8EB406948947F0F69E460BFEA19E778D7D3619AFE3EBEF3CEACA10A289F303E3EA717850757B43948FC3C6AE766B4AA8A08F927F976A0313DCB6054860DFF1CDE7DF5C87E2E3147CC9DCDDF31C97282B975088DF94679A4F1E53EFEBF68FC3717D28D00E51BED299CB1815777265B0DB4576CC314C82612277626F82D", t = new RSAKey;
    t.setPublic(e, "10001");
    var n = t.encrypt("1234567890abcdefghijklmnopqrstuvwxyz<>?,./:;'{}[]!@#$%^&*_+|=-");
    document.getElementById("txt1").value = n
}

var aaa='Netscape';
var appVersion='5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36';


var dbits, canary = 0xdeadbeefcafe, j_lm = (canary & 16777215) == 15715070;
j_lm && aaa == "Microsoft Internet Explorer" ? (BigInteger.prototype.am = am2, dbits = 30) : j_lm && aaa != "Netscape" ? (BigInteger.prototype.am = am1, dbits = 26) : (BigInteger.prototype.am = am3, dbits = 28), BigInteger.prototype.DB = dbits, BigInteger.prototype.DM = (1 << dbits) - 1, BigInteger.prototype.DV = 1 << dbits;
var BI_FP = 52;
BigInteger.prototype.FV = Math.pow(2, BI_FP), BigInteger.prototype.F1 = BI_FP - dbits, BigInteger.prototype.F2 = 2 * dbits - BI_FP;
var BI_RM = "0123456789abcdefghijklmnopqrstuvwxyz", BI_RC = new Array, rr, vv;
rr = "0".charCodeAt(0);
for (vv = 0; vv <= 9; ++vv)
    BI_RC[rr++] = vv;
rr = "a".charCodeAt(0);
for (vv = 10; vv < 36; ++vv)
    BI_RC[rr++] = vv;
rr = "A".charCodeAt(0);
for (vv = 10; vv < 36; ++vv)
    BI_RC[rr++] = vv;
Classic.prototype.convert = cConvert, Classic.prototype.revert = cRevert, Classic.prototype.reduce = cReduce, Classic.prototype.mulTo = cMulTo, Classic.prototype.sqrTo = cSqrTo, Montgomery.prototype.convert = montConvert, Montgomery.prototype.revert = montRevert, Montgomery.prototype.reduce = montReduce, Montgomery.prototype.mulTo = montMulTo, Montgomery.prototype.sqrTo = montSqrTo, BigInteger.prototype.copyTo = bnpCopyTo, BigInteger.prototype.fromInt = bnpFromInt, BigInteger.prototype.fromString = bnpFromString, BigInteger.prototype.clamp = bnpClamp, BigInteger.prototype.dlShiftTo = bnpDLShiftTo, BigInteger.prototype.drShiftTo = bnpDRShiftTo, BigInteger.prototype.lShiftTo = bnpLShiftTo, BigInteger.prototype.rShiftTo = bnpRShiftTo, BigInteger.prototype.subTo = bnpSubTo, BigInteger.prototype.multiplyTo = bnpMultiplyTo, BigInteger.prototype.squareTo = bnpSquareTo, BigInteger.prototype.divRemTo = bnpDivRemTo, BigInteger.prototype.invDigit = bnpInvDigit, BigInteger.prototype.isEven = bnpIsEven, BigInteger.prototype.exp = bnpExp, BigInteger.prototype.toString = bnToString, BigInteger.prototype.negate = bnNegate, BigInteger.prototype.abs = bnAbs, BigInteger.prototype.compareTo = bnCompareTo, BigInteger.prototype.bitLength = bnBitLength, BigInteger.prototype.mod = bnMod, BigInteger.prototype.modPowInt = bnModPowInt, BigInteger.ZERO = nbv(0), BigInteger.ONE = nbv(1), Arcfour.prototype.init = ARC4init, Arcfour.prototype.next = ARC4next;
var rng_psize = 256, rng_state, rng_pool, rng_pptr;
if (rng_pool == null) {
    rng_pool = new Array, rng_pptr = 0;
    var t;
// if (aaa== "Netscape" && appVersion < "5" && window.crypto) {
// var z = window.crypto.random(32);
// for (t = 0; t < z.length; ++t)
// rng_pool[rng_pptr++] = z.charCodeAt(t) & 255
// }
    while (rng_pptr < rng_psize)
        t = Math.floor(65536 * Math.random()), rng_pool[rng_pptr++] = t >>> 8, rng_pool[rng_pptr++] = t & 255;
    rng_pptr = 0, rng_seed_time()
}
SecureRandom.prototype.nextBytes = rng_get_bytes, RSAKey.prototype.doPublic = RSADoPublic, RSAKey.prototype.setPublic = RSASetPublic, RSAKey.prototype.encrypt = RSAEncrypt;
var b64map = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", b64pad = "=";


function rsa(password,publicKey,rsaExponent ) {
    var e = new RSAKey;
// var publicKey =
// 'BE24E372DC1B329633A6A014A7C02797915E3C363DD6EE119377BD645329B7E6446B4A71AC5F878EBC870C6D8BFD3C06B92E6C6E93390B34192A7A9E430800091761473FAC2CC0A68A828B2589A8CB729C19161E8E27F4C0F3CDE9701FAFE48D2B65947799072AFA6A3F2D7BDBEF8B6D7429C2D115A3E5F723467D57B3AC6967';
// var rsaExponent = '10001';
    e.setPublic(publicKey, rsaExponent);
    var t = e.encrypt(password);
    
    return t;
}