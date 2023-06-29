<#import "base.ftl" as base>
<#import "yandex.ftl" as yandex>

<!DOCTYPE html>
<@base.layout>
<@base.header/>
<@base.breadcrumbs/>
  <div id="search-line-mobile" class="search-line-mobile">
      <@yandex.searchLine />
  </div>

  <div id="content" class="content">
    <div class="left scroll-bar">
        <@base.toc/>
    </div>
  </div>
  <div id="main" class="main">
    <div class="no-file">
        ${title}: ${resourceName}
    </div>
  </div>
<@base.footer/>
</@base.layout>
