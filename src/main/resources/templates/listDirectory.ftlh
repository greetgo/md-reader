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
<@base.footer/>
</@base.layout>
