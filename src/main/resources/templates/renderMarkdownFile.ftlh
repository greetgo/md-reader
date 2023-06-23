<#import "base.ftl" as base>
<!DOCTYPE html>
<@base.layout>
<@base.header/>
<@base.breadcrumbs/>
  <div class="content">
    <div class="left scroll-bar">
        <@base.toc/>
    </div>
  </div>
  <div class="main">
    <div class="markdown">
        <#outputformat "plainText">
            ${html}
        </#outputformat>
    </div>
  </div>
<@base.footer/>
</@base.layout>
