        <%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.springframework.web.servlet.DispatcherServlet"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.gs1us.sgl.serviceterms.TermsOfService"%>
</div>
        <footer>
            <hr />
                <div class="container">
                    <p class="small">Copyright &copy; 2016 <%= WebappUtil.productCopyrightHolder() %>; All Rights Reserved
                    <a href="http://www.gs1us.org/privacy-policy" target="_blank">Privacy Policy</a>
                    | <a href="http://www.gs1us.org/terms-of-use" target="_blank">Terms of Use</a>
                    | <a href="http://www.gs1us.org/gs1-us-antitrust-compliance-policy" target="_blank">Antitrust Policy</a>
                </div>
            </footer>
            <!-- End Footer -->
            <!-- Back to Top -->
            <span id="top-link-block" class="hidden visible-xs">
            <a href="#top" class="btn btn-secondary" role="button" onclick="$('html,body').animate({scrollTop:0},'slow');return false;">
            <i class="glyphicon glyphicon-chevron-up"></i> Back to Top
            </a>
            </span>
            <!-- Back to Top -->
        </div>

        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<%= request.getContextPath() + "/js/bootstrap.min.js" %>"></script>
        <!-- Custom js to instantiate tooltips, alerts, modals, etc -->
        <!-- 
        <script src="<%= request.getContextPath() + "/js/documentation.js" %>"></script>
         -->
        <script src="<%= request.getContextPath() + "/js/documentation-customized.js" %>"></script>
        <!-- Calendar Date Picker plugin and options -->
        <script src="<%= request.getContextPath() + "/js/calendar-plugin/bootstrap-datepicker.js" %>"></script>
        <script type="text/javascript">
        $('.form-date-picker').datepicker({
        todayBtn: "linked",
        multidate: false,
        autoclose: true,
        todayHighlight: true,
        format: "mm/dd/yyyy"
        });
        </script>
<!-- 
        <script type="text/javascript">
            // Back to Top link for small form factor devices only
            // Only enables if the document has a long scroll bar (Note the window height + offset)
            if (($(window).height() + 100) < $(document).height()) {
                $('#top-link-block').removeClass('hidden').affix({
                    // how far to scroll down before link "slides" into view
                    offset: {top: 100}
                });
            }
        </script>
-->
        <!-- Lazy load Bootstrap accessibility plugin-->
        <script src="<%= request.getContextPath() + "/js/accessibility-customized.js" %>"></script>
        
<!-- 
        <script>
        $('.ae-del-context').delegate('.ae-del-button', 'click', function(e) { 
            var $myListItem = $(this); 
			$myListItem.closest('.ae-del-item').remove();
			e.preventDefault();
        }); 
		$('.ae-add').click(function (e) {
			var $addGroupName = $(this).attr('ae-add-group');
			var $addGroup = $('#' + $addGroupName);
			var $itemIndex = Number($addGroup.children().last().attr('ae-index')) + 1;
			var $indexVar = new RegExp('{'+ $addGroupName + '}', 'g');
			var $rowTemplate = $(this).attr('ae-add-template');
			$addGroup.append($($rowTemplate.replace($indexVar, $itemIndex)));
			e.preventDefault();
		});
		</script>
 -->
    </body>
</html>
