<#import "base.ftl" as base>
<@base.layout>
  <div>
    Show DIRECTORY ${dirPath}
  </div>
</@base.layout>
<@base.layout>
    <@base.header/>
  <div class="content">
    <div class="left">
        <@base.toc/>
    </div>
    <div class="main">
      <div class="caption">${caption}</div>
      <div class="dir-list">
          <#list dirItems as item>
            <div class="dir-item">
              <a href="${item.reference}">${item.caption}</a>
            </div>
          </#list>
      </div>
    </div>
  </div>
    <@base.footer/>
</@base.layout>
