<#import "yandex.ftl" as yandex>

<#macro layout>
  <html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <link rel="icon" href="${host}/favicon.ico" type="image/x-icon">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
      <#--noinspection FtlReferencesInspection-->
    <title>${title}</title>

    <link rel="stylesheet" href="/static/font.css">
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
      <md-ref href="/"><img class="header-logo-img" src="/logo.png" alt="mybpm!"/></md-ref>
    </div>
    <div class="search-line">
        <@yandex.searchLine />
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
  <div class="download-toc">
    <span>Скачать в формате</span>
    <a href="${tocDownloadReferencePDF}">PDF</a>
    <a href="${tocDownloadReferenceDOCX}">DOCX</a>
  </div>
</#macro>

<#macro footer>
  <div class="footer">
  </div>
</#macro>
