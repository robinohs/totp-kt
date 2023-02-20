"use strict";(self.webpackChunkdocs=self.webpackChunkdocs||[]).push([[9233],{3905:(e,r,t)=>{t.d(r,{Zo:()=>p,kt:()=>y});var n=t(7294);function o(e,r,t){return r in e?Object.defineProperty(e,r,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[r]=t,e}function a(e,r){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);r&&(n=n.filter((function(r){return Object.getOwnPropertyDescriptor(e,r).enumerable}))),t.push.apply(t,n)}return t}function i(e){for(var r=1;r<arguments.length;r++){var t=null!=arguments[r]?arguments[r]:{};r%2?a(Object(t),!0).forEach((function(r){o(e,r,t[r])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):a(Object(t)).forEach((function(r){Object.defineProperty(e,r,Object.getOwnPropertyDescriptor(t,r))}))}return e}function c(e,r){if(null==e)return{};var t,n,o=function(e,r){if(null==e)return{};var t,n,o={},a=Object.keys(e);for(n=0;n<a.length;n++)t=a[n],r.indexOf(t)>=0||(o[t]=e[t]);return o}(e,r);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(n=0;n<a.length;n++)t=a[n],r.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(o[t]=e[t])}return o}var s=n.createContext({}),l=function(e){var r=n.useContext(s),t=r;return e&&(t="function"==typeof e?e(r):i(i({},r),e)),t},p=function(e){var r=l(e.components);return n.createElement(s.Provider,{value:r},e.children)},d="mdxType",u={inlineCode:"code",wrapper:function(e){var r=e.children;return n.createElement(n.Fragment,{},r)}},g=n.forwardRef((function(e,r){var t=e.components,o=e.mdxType,a=e.originalType,s=e.parentName,p=c(e,["components","mdxType","originalType","parentName"]),d=l(t),g=o,y=d["".concat(s,".").concat(g)]||d[g]||u[g]||a;return t?n.createElement(y,i(i({ref:r},p),{},{components:t})):n.createElement(y,i({ref:r},p))}));function y(e,r){var t=arguments,o=r&&r.mdxType;if("string"==typeof e||o){var a=t.length,i=new Array(a);i[0]=g;var c={};for(var s in r)hasOwnProperty.call(r,s)&&(c[s]=r[s]);c.originalType=e,c[d]="string"==typeof e?e:o,i[1]=c;for(var l=2;l<a;l++)i[l]=t[l];return n.createElement.apply(null,i)}return n.createElement.apply(null,t)}g.displayName="MDXCreateElement"},271:(e,r,t)=>{t.r(r),t.d(r,{assets:()=>s,contentTitle:()=>i,default:()=>u,frontMatter:()=>a,metadata:()=>c,toc:()=>l});var n=t(7462),o=(t(7294),t(3905));const a={sidebar_position:1},i="Initialize Generator",c={unversionedId:"secretgenerator/recovery/initialize-generator",id:"secretgenerator/recovery/initialize-generator",title:"Initialize Generator",description:"The recovery-code generator is used to generate recovery-codes, which are randomly generated strings in block form.",source:"@site/docs/secretgenerator/recovery/initialize-generator.mdx",sourceDirName:"secretgenerator/recovery",slug:"/secretgenerator/recovery/initialize-generator",permalink:"/totp-kt/docs/next/secretgenerator/recovery/initialize-generator",draft:!1,editUrl:"https://github.com/robinohs/totp-kt/docs/secretgenerator/recovery/initialize-generator.mdx",tags:[],version:"current",sidebarPosition:1,frontMatter:{sidebar_position:1},sidebar:"tutorialSidebar",previous:{title:"Recovery-Code Generator",permalink:"/totp-kt/docs/next/category/recovery-code-generator"},next:{title:"Generate Recovery-Codes",permalink:"/totp-kt/docs/next/secretgenerator/recovery/generate-recovery-codes"}},s={},l=[{value:"Create recovery-code generator",id:"create-recovery-code-generator",level:2},{value:"Spring Boot",id:"spring-boot",level:2},{value:"Customize properties",id:"customize-properties",level:2},{value:"Code length",id:"code-length",level:3}],p={toc:l},d="wrapper";function u(e){let{components:r,...t}=e;return(0,o.kt)(d,(0,n.Z)({},p,t,{components:r,mdxType:"MDXLayout"}),(0,o.kt)("h1",{id:"initialize-generator"},"Initialize Generator"),(0,o.kt)("p",null,"The recovery-code generator is used to generate recovery-codes, which are randomly generated strings in block form."),(0,o.kt)("h2",{id:"create-recovery-code-generator"},"Create recovery-code generator"),(0,o.kt)("p",null,"You can create an instance of the RecoveryCodeGenerator in the following way:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},"val recoveryCodeGenerator = RecoveryCodeGenerator()\n")),(0,o.kt)("h2",{id:"spring-boot"},"Spring Boot"),(0,o.kt)("p",null,"Instead of creating a new instance of a generator each time a token is checked, it is also possible to create a bean within Spring.\nThis allows to configure the generator once and this configuration is maintained each time the bean is injected into a component."),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},"@Bean\nfun recoveryCodeGenerator(): RecoveryCodeGenerator {\n    val generator = RecoveryCodeGenerator()\n    generator.blockLength = 12\n    generator.numberOfBlocks = 6\n    return generator\n}\n")),(0,o.kt)("p",null,"This bean can then be injected in the constructor of any class marked with @Component (@Service, ...)."),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},"@Component\nclass CustomComponent(private val recoveryCodeGenerator: RecoveryCodeGenerator) {\n    //...\n}\n")),(0,o.kt)("h2",{id:"customize-properties"},"Customize properties"),(0,o.kt)("p",null,"It is possible to customize the properties of the generator, either by setters or applying them in the constructor."),(0,o.kt)("h3",{id:"code-length"},"Code length"),(0,o.kt)("p",null,"The code length specifies how long a generated code will be. If the code length is changed, it is necessary that the user's authenticator app supports this as well."),(0,o.kt)("admonition",{title:"Could break logic",type:"danger"},(0,o.kt)("p",{parentName:"admonition"},"Most authenticator apps, such as those from Microsoft or Google, use a length of 6 digits. If you change this number, they are no longer usable.\nHowever, remember that that they are widely used before you drop support for them.")))}u.isMDXComponent=!0}}]);