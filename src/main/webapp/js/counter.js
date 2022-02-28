$(document).ready(function() {
    $('.counter_btn_minus').click(function () {
        var $input = $(this).parent().find('input');
        let count = parseInt($input.val()) - 1;
        count = count < 1 ? 1 : count;
        $input.val(count);
        $input.change();
        return false;
    });
    $('.counter_btn_plus').click(function () {
        var $input = $(this).parent().find('input');
        let count = parseInt($input.val()) + 1;
        count = count > 50 ? 50 : count;
        $input.val(count);
        $input.change();
        return false;
    });
});