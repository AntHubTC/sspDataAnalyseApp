var $=typeof globalThis<"u"?globalThis:typeof window<"u"?window:typeof global<"u"?global:typeof self<"u"?self:{};function B(b){return b&&b.__esModule&&Object.prototype.hasOwnProperty.call(b,"default")?b.default:b}function W(b){if(b.__esModule)return b;var T=b.default;if(typeof T=="function"){var S=function E(){if(this instanceof E){var g=[null];g.push.apply(g,arguments);var s=Function.bind.apply(T,g);return new s}return T.apply(this,arguments)};S.prototype=T.prototype}else S={};return Object.defineProperty(S,"__esModule",{value:!0}),Object.keys(b).forEach(function(E){var g=Object.getOwnPropertyDescriptor(b,E);Object.defineProperty(S,E,g.get?g:{enumerable:!0,get:function(){return b[E]}})}),S}var R={exports:{}};/*!
 * clipboard.js v2.0.11
 * https://clipboardjs.com/
 *
 * Licensed MIT © Zeno Rocha
 */(function(b,T){(function(E,g){b.exports=g()})($,function(){return function(){var S={686:function(s,c,t){t.d(c,{default:function(){return X}});var a=t(279),f=t.n(a),l=t(370),h=t.n(l),y=t(817),m=t.n(y);function d(i){try{return document.execCommand(i)}catch{return!1}}var v=function(n){var e=m()(n);return d("cut"),e},p=v;function w(i){var n=document.documentElement.getAttribute("dir")==="rtl",e=document.createElement("textarea");e.style.fontSize="12pt",e.style.border="0",e.style.padding="0",e.style.margin="0",e.style.position="absolute",e.style[n?"right":"left"]="-9999px";var r=window.pageYOffset||document.documentElement.scrollTop;return e.style.top="".concat(r,"px"),e.setAttribute("readonly",""),e.value=i,e}var M=function(n,e){var r=w(n);e.container.appendChild(r);var o=m()(r);return d("copy"),r.remove(),o},k=function(n){var e=arguments.length>1&&arguments[1]!==void 0?arguments[1]:{container:document.body},r="";return typeof n=="string"?r=M(n,e):n instanceof HTMLInputElement&&!["text","search","url","tel","password"].includes(n==null?void 0:n.type)?r=M(n.value,e):(r=m()(n),d("copy")),r},P=k;function A(i){"@babel/helpers - typeof";return typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?A=function(e){return typeof e}:A=function(e){return e&&typeof Symbol=="function"&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},A(i)}var D=function(){var n=arguments.length>0&&arguments[0]!==void 0?arguments[0]:{},e=n.action,r=e===void 0?"copy":e,o=n.container,u=n.target,_=n.text;if(r!=="copy"&&r!=="cut")throw new Error('Invalid "action" value, use either "copy" or "cut"');if(u!==void 0)if(u&&A(u)==="object"&&u.nodeType===1){if(r==="copy"&&u.hasAttribute("disabled"))throw new Error('Invalid "target" attribute. Please use "readonly" instead of "disabled" attribute');if(r==="cut"&&(u.hasAttribute("readonly")||u.hasAttribute("disabled")))throw new Error(`Invalid "target" attribute. You can't cut text from elements with "readonly" or "disabled" attributes`)}else throw new Error('Invalid "target" value, use a valid Element');if(_)return P(_,{container:o});if(u)return r==="cut"?p(u):P(u,{container:o})},F=D;function x(i){"@babel/helpers - typeof";return typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?x=function(e){return typeof e}:x=function(e){return e&&typeof Symbol=="function"&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},x(i)}function H(i,n){if(!(i instanceof n))throw new TypeError("Cannot call a class as a function")}function N(i,n){for(var e=0;e<n.length;e++){var r=n[e];r.enumerable=r.enumerable||!1,r.configurable=!0,"value"in r&&(r.writable=!0),Object.defineProperty(i,r.key,r)}}function I(i,n,e){return n&&N(i.prototype,n),e&&N(i,e),i}function z(i,n){if(typeof n!="function"&&n!==null)throw new TypeError("Super expression must either be null or a function");i.prototype=Object.create(n&&n.prototype,{constructor:{value:i,writable:!0,configurable:!0}}),n&&L(i,n)}function L(i,n){return L=Object.setPrototypeOf||function(r,o){return r.__proto__=o,r},L(i,n)}function U(i){var n=J();return function(){var r=O(i),o;if(n){var u=O(this).constructor;o=Reflect.construct(r,arguments,u)}else o=r.apply(this,arguments);return Y(this,o)}}function Y(i,n){return n&&(x(n)==="object"||typeof n=="function")?n:G(i)}function G(i){if(i===void 0)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return i}function J(){if(typeof Reflect>"u"||!Reflect.construct||Reflect.construct.sham)return!1;if(typeof Proxy=="function")return!0;try{return Date.prototype.toString.call(Reflect.construct(Date,[],function(){})),!0}catch{return!1}}function O(i){return O=Object.setPrototypeOf?Object.getPrototypeOf:function(e){return e.__proto__||Object.getPrototypeOf(e)},O(i)}function j(i,n){var e="data-clipboard-".concat(i);if(n.hasAttribute(e))return n.getAttribute(e)}var V=function(i){z(e,i);var n=U(e);function e(r,o){var u;return H(this,e),u=n.call(this),u.resolveOptions(o),u.listenClick(r),u}return I(e,[{key:"resolveOptions",value:function(){var o=arguments.length>0&&arguments[0]!==void 0?arguments[0]:{};this.action=typeof o.action=="function"?o.action:this.defaultAction,this.target=typeof o.target=="function"?o.target:this.defaultTarget,this.text=typeof o.text=="function"?o.text:this.defaultText,this.container=x(o.container)==="object"?o.container:document.body}},{key:"listenClick",value:function(o){var u=this;this.listener=h()(o,"click",function(_){return u.onClick(_)})}},{key:"onClick",value:function(o){var u=o.delegateTarget||o.currentTarget,_=this.action(u)||"copy",C=F({action:_,container:this.container,target:this.target(u),text:this.text(u)});this.emit(C?"success":"error",{action:_,text:C,trigger:u,clearSelection:function(){u&&u.focus(),window.getSelection().removeAllRanges()}})}},{key:"defaultAction",value:function(o){return j("action",o)}},{key:"defaultTarget",value:function(o){var u=j("target",o);if(u)return document.querySelector(u)}},{key:"defaultText",value:function(o){return j("text",o)}},{key:"destroy",value:function(){this.listener.destroy()}}],[{key:"copy",value:function(o){var u=arguments.length>1&&arguments[1]!==void 0?arguments[1]:{container:document.body};return P(o,u)}},{key:"cut",value:function(o){return p(o)}},{key:"isSupported",value:function(){var o=arguments.length>0&&arguments[0]!==void 0?arguments[0]:["copy","cut"],u=typeof o=="string"?[o]:o,_=!!document.queryCommandSupported;return u.forEach(function(C){_=_&&!!document.queryCommandSupported(C)}),_}}]),e}(f()),X=V},828:function(s){var c=9;if(typeof Element<"u"&&!Element.prototype.matches){var t=Element.prototype;t.matches=t.matchesSelector||t.mozMatchesSelector||t.msMatchesSelector||t.oMatchesSelector||t.webkitMatchesSelector}function a(f,l){for(;f&&f.nodeType!==c;){if(typeof f.matches=="function"&&f.matches(l))return f;f=f.parentNode}}s.exports=a},438:function(s,c,t){var a=t(828);function f(y,m,d,v,p){var w=h.apply(this,arguments);return y.addEventListener(d,w,p),{destroy:function(){y.removeEventListener(d,w,p)}}}function l(y,m,d,v,p){return typeof y.addEventListener=="function"?f.apply(null,arguments):typeof d=="function"?f.bind(null,document).apply(null,arguments):(typeof y=="string"&&(y=document.querySelectorAll(y)),Array.prototype.map.call(y,function(w){return f(w,m,d,v,p)}))}function h(y,m,d,v){return function(p){p.delegateTarget=a(p.target,m),p.delegateTarget&&v.call(y,p)}}s.exports=l},879:function(s,c){c.node=function(t){return t!==void 0&&t instanceof HTMLElement&&t.nodeType===1},c.nodeList=function(t){var a=Object.prototype.toString.call(t);return t!==void 0&&(a==="[object NodeList]"||a==="[object HTMLCollection]")&&"length"in t&&(t.length===0||c.node(t[0]))},c.string=function(t){return typeof t=="string"||t instanceof String},c.fn=function(t){var a=Object.prototype.toString.call(t);return a==="[object Function]"}},370:function(s,c,t){var a=t(879),f=t(438);function l(d,v,p){if(!d&&!v&&!p)throw new Error("Missing required arguments");if(!a.string(v))throw new TypeError("Second argument must be a String");if(!a.fn(p))throw new TypeError("Third argument must be a Function");if(a.node(d))return h(d,v,p);if(a.nodeList(d))return y(d,v,p);if(a.string(d))return m(d,v,p);throw new TypeError("First argument must be a String, HTMLElement, HTMLCollection, or NodeList")}function h(d,v,p){return d.addEventListener(v,p),{destroy:function(){d.removeEventListener(v,p)}}}function y(d,v,p){return Array.prototype.forEach.call(d,function(w){w.addEventListener(v,p)}),{destroy:function(){Array.prototype.forEach.call(d,function(w){w.removeEventListener(v,p)})}}}function m(d,v,p){return f(document.body,d,v,p)}s.exports=l},817:function(s){function c(t){var a;if(t.nodeName==="SELECT")t.focus(),a=t.value;else if(t.nodeName==="INPUT"||t.nodeName==="TEXTAREA"){var f=t.hasAttribute("readonly");f||t.setAttribute("readonly",""),t.select(),t.setSelectionRange(0,t.value.length),f||t.removeAttribute("readonly"),a=t.value}else{t.hasAttribute("contenteditable")&&t.focus();var l=window.getSelection(),h=document.createRange();h.selectNodeContents(t),l.removeAllRanges(),l.addRange(h),a=l.toString()}return a}s.exports=c},279:function(s){function c(){}c.prototype={on:function(t,a,f){var l=this.e||(this.e={});return(l[t]||(l[t]=[])).push({fn:a,ctx:f}),this},once:function(t,a,f){var l=this;function h(){l.off(t,h),a.apply(f,arguments)}return h._=a,this.on(t,h,f)},emit:function(t){var a=[].slice.call(arguments,1),f=((this.e||(this.e={}))[t]||[]).slice(),l=0,h=f.length;for(l;l<h;l++)f[l].fn.apply(f[l].ctx,a);return this},off:function(t,a){var f=this.e||(this.e={}),l=f[t],h=[];if(l&&a)for(var y=0,m=l.length;y<m;y++)l[y].fn!==a&&l[y].fn._!==a&&h.push(l[y]);return h.length?f[t]=h:delete f[t],this}},s.exports=c,s.exports.TinyEmitter=c}},E={};function g(s){if(E[s])return E[s].exports;var c=E[s]={exports:{}};return S[s](c,c.exports,g),c.exports}return function(){g.n=function(s){var c=s&&s.__esModule?function(){return s.default}:function(){return s};return g.d(c,{a:c}),c}}(),function(){g.d=function(s,c){for(var t in c)g.o(c,t)&&!g.o(s,t)&&Object.defineProperty(s,t,{enumerable:!0,get:c[t]})}}(),function(){g.o=function(s,c){return Object.prototype.hasOwnProperty.call(s,c)}}(),g(686)}().default})})(R);var K=R.exports;const Z=B(K);export{Z as C,W as a,$ as c,B as g};
