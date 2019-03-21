/**
 * jQuery Plugin for creating collapsible fieldset.
 *
 * Copyright (c) 2013 Mirza Busatlic
 */

(function($) {
  
	$.fn.collapsible = function(options) {
		
		var settings = $.extend({
			collapsed: false, 
			animation: false
		}, options);
		
		this.each(function() {
			var $fieldset = $(this);
			var $legend = $fieldset.children("legend");
			var isCollapsed = $fieldset.hasClass("collapsed");
			
			$legend.click(function() {
				collapse($fieldset, settings, !isCollapsed);
				isCollapsed = !isCollapsed;
			});
			
			// Perform initial collapse.
			// Don't use animation to close for initial collapse.
			if(isCollapsed) {
				collapse($fieldset, {animation: false}, isCollapsed);
			} else {
				collapse($fieldset, settings, isCollapsed);
			}
			
		});
	};
	
	/**
	 * Collapse/uncollapse the specified fieldset.
	 * @param {object} $fieldset
	 * @param {object} options
	 * @param {boolean} collapse
	 */
	function collapse($fieldset, options, doCollapse) {
		$container = $fieldset.find("div");
		if(doCollapse) {
			if(options.animation) {
				$container.slideUp(options.speed);
			} else {
				$container.hide();
			}
			$fieldset.removeClass("expanded").addClass("collapsed");
		} else {
			if(options.animation) {
				$container.slideDown(options.speed);
			} else {
				$container.show();
			}
			$fieldset.removeClass("collapsed").addClass("expanded");
		}
	};
	
})(jQuery);
