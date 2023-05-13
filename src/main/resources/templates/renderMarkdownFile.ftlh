<#import "base.ftl" as base>
<@base.layout>
    <@base.header/>
  <div class="content">
    <div class="left">
        <@base.toc/>
    </div>
    <div class="main">
      <div class="markdown">
          <#outputformat "plainText">
              ${html}
          </#outputformat>
      </div>
    </div>
  </div>
    <@base.footer/>
</@base.layout>
