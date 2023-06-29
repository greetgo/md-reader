<#import "base.ftl" as base>
<#import "yandex.ftl" as yandex>

<!DOCTYPE html>
<@base.layout>
<@base.header/>
<@base.breadcrumbs/>
  <div id="search-line-mobile" class="search-line-mobile">
      <@yandex.searchLine />
  </div>

  <div>
      <@base.documents/>
  </div>

  <div id="content" class="content">
    <div class="left scroll-bar">
        <@base.toc/>
    </div>
  </div>
  <div id="main" class="main">
    <div class="markdown">
        <#outputformat "plainText">
            ${html}
        </#outputformat>
    </div>
  </div>
<@base.footer/>
</@base.layout>
