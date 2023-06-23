<#import "base.ftl" as base>
<!DOCTYPE html>
<@base.layout>
<@base.header/>
<@base.breadcrumbs/>
  <div class="content">
    <div class="left scroll-bar">
        <@base.toc/>
    </div>
    <div class="main">
      <div class="dir-list">
        <div class="caption">${caption}</div>
        <div class="dir-list-items">
            <#list dirItems as item>
              <div class="dir-item">
                <a href="${item.reference}">${item.caption}</a>
              </div>
            </#list>
        </div>
      </div>
    </div>
  </div>
<@base.footer/>
</@base.layout>
