<#macro layout>
  <html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
      <#--noinspection FtlReferencesInspection-->
    <title>${title}</title>

    <link rel="stylesheet" href="/static/common.css">
    <link rel="stylesheet" href="/static/header.css">
    <link rel="stylesheet" href="/static/breadcrumbs.css">
    <link rel="stylesheet" href="/static/content.css">
    <link rel="stylesheet" href="/static/markdown.css">
    <link rel="stylesheet" href="/static/dir-list.css">
    <link rel="stylesheet" href="/static/no-file.css">
    <link rel="stylesheet" href="/static/footer.css">
    <link rel="stylesheet" href="/static/md-ref.css">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto+Condensed:ital,wght@0,300;0,400;0,700;1,300;1,400;1,700&display=swap"
          rel="stylesheet">

  </head>
  <body>
  <#nested/>
  <script src="/static/md-ref.js"></script>
  </body>
  </html>
</#macro>

<#macro header>
  <div class="header">
    <div class="header-caption">
        <#--noinspection FtlReferencesInspection-->
      <a href="/">${headerCaption}</a>
    </div>
    <div class="search-line">
      <div class="ya-site-form ya-site-form_inited_no" data-bem="{&quot;action&quot;:&quot;https://yandex.ru/search/site/&quot;,&quot;arrow&quot;:true,&quot;bg&quot;:&quot;#cccccc&quot;,&quot;fontsize&quot;:12,&quot;fg&quot;:&quot;#000000&quot;,&quot;language&quot;:&quot;ru&quot;,&quot;logo&quot;:&quot;rb&quot;,&quot;publicname&quot;:&quot;Поиск по doc.mybpm.kz&quot;,&quot;suggest&quot;:true,&quot;target&quot;:&quot;_blank&quot;,&quot;tld&quot;:&quot;ru&quot;,&quot;type&quot;:2,&quot;usebigdictionary&quot;:true,&quot;searchid&quot;:2897809,&quot;input_fg&quot;:&quot;#000000&quot;,&quot;input_bg&quot;:&quot;#ffffff&quot;,&quot;input_fontStyle&quot;:&quot;normal&quot;,&quot;input_fontWeight&quot;:&quot;normal&quot;,&quot;input_placeholder&quot;:&quot;&quot;,&quot;input_placeholderColor&quot;:&quot;#000000&quot;,&quot;input_borderColor&quot;:&quot;#999999&quot;}"><form action="https://yandex.ru/search/site/" method="get" target="_blank" accept-charset="utf-8"><input type="hidden" name="searchid" value="2897809"/><input type="hidden" name="l10n" value="ru"/><input type="hidden" name="reqenc" value=""/><input type="search" name="text" value=""/><input type="submit" value="Найти"/></form></div><style type="text/css">.ya-page_js_yes .ya-site-form_inited_no { display: none; }</style><script type="text/javascript">(function(w,d,c){var s=d.createElement('script'),h=d.getElementsByTagName('script')[0],e=d.documentElement;if((' '+e.className+' ').indexOf(' ya-page_js_yes ')===-1){e.className+=' ya-page_js_yes';}s.type='text/javascript';s.async=true;s.charset='utf-8';s.src=(d.location.protocol==='https:'?'https:':'http:')+'//site.yandex.net/v2.0/js/all.js';h.parentNode.insertBefore(s,h);(w[c]||(w[c]=[])).push(function(){Ya.Site.Form.init()})})(window,document,'yandex_site_callbacks');</script>
    </div>
  </div>
</#macro>
<#macro breadcrumbs>
  <div class="breadcrumbs">
      <#--noinspection FtlReferencesInspection-->
      <#list breadcrumbsItems as item>
        <div class="breadcrumbs-item">
          <md-ref href="${item.reference}">${item.caption}</md-ref>
        </div>
          <#sep>
            <div class="breadcrumbs-item-separator">/</div>
      </#list>
  </div>
</#macro>

<#macro toc>
<#--noinspection FtlReferencesInspection-->
    <#list tocItems as item>
      <div class="caption level${item.level}${item.selected?then(' selected', '')}">
        <a href="${item.reference}">${item.caption}</a>
      </div>
    </#list>
<#--    <div class="download-toc">-->
<#--      <a href="${tocDownloadReference}">Скачать в формате PDF</a>-->
<#--    </div>-->
</#macro>

<#macro footer>
  <div class="footer">
  </div>
</#macro>
