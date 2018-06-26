/**
 *
 * Slide up Slide down block-title block
 *
 * */

jQuery(function ($) {

    var breakpointLargeScreen = 1025;

    function addDefaultClass() {
        $('.block.accordion').each(function () {
            if (!$(this).hasClass('accordion-plus') && !$(this).hasClass('accordion-minus')) {
                if (window.matchMedia("(max-width:" + breakpointLargeScreen + "px)").matches) {
                    $(this).addClass('accordion-plus');
                } else {
                    $(this).addClass('accordion-minus');
                }
            }
        });
    }

    function accordionProcess(e) {
        $('.block.accordion').each(function () {
            if (window.matchMedia("(max-width:" + breakpointLargeScreen + "px)").matches) {
                $(this).children('.content').hide();
                if ($(this).hasClass('accordion-minus')) {
                    $(this).removeClass('accordion-minus').addClass('accordion-plus');
                }
            }
            else{
                if ($(this).hasClass('accordion-plus')) {
                    $(this).children('.content').hide();
                }
            }
        });

        $('.block.accordion > h2').click(function (e) {
            var selected_block = $(this).parent();

            var $id_block = '#' + selected_block.attr('id').replace(':','\\:');
            if (selected_block.hasClass('accordion-minus')) {
                $($id_block + ' .content').hide();
                selected_block.removeClass('accordion-minus').addClass('accordion-plus');
                $(this).parent().removeClass('accordion-on').addClass('accordion-off');
            } else {
                $($id_block + ' .content').show();
                $(this).parent().removeClass('accordion-off').addClass('accordion-on');
                selected_block.removeClass('accordion-plus').addClass('accordion-minus');
            }
            return false;
        });
    }

    window.addEventListener('resize', accordionProcess, false);
    addDefaultClass();
    accordionProcess();

});