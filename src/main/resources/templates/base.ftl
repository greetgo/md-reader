<#macro layout>
  <!doctype html>
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
    <link rel="stylesheet" href="/static/content-left.css">
    <link rel="stylesheet" href="/static/markdown.css">
    <link rel="stylesheet" href="/static/dir-list.css">
    <link rel="stylesheet" href="/static/no-file.css">
    <link rel="stylesheet" href="/static/footer.css">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto+Condensed:ital,wght@0,300;0,400;0,700;1,300;1,400;1,700&display=swap"
          rel="stylesheet">

  </head>
  <body>
  <#nested/>
  </body>
  </html>
</#macro>

<#macro header>
  <div class="header">Header</div>
</#macro>
<#macro breadcrumbs>
  <div class="breadcrumbs">Хлебные крошки</div>
</#macro>

<#macro toc>

<#--noinspection FtlReferencesInspection-->
    <#list tocItems as item>
      <div class="caption level${item.level}${item.selected?then(' selected', '')}">
        <a href="${item.reference}">${item.caption}</a>
      </div>
    </#list>

</#macro>
<#macro footer>
  <div class="footer">Footer</div>
</#macro>
