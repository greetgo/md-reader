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
      <div class="no-file">
          ${title}: ${resourceName}
      </div>
    </div>
  </div>
<@base.footer/>
</@base.layout>
