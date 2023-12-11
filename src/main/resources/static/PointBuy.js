

let pointsToSpend;
pointsToSpend = 100;
maxStat = 50;
pointsMax = pointsToSpend;

jQuery(document).ready(function () {
    $('.pointsToSpend').append(pointsToSpend);
    $('.pointsMax').append(pointsMax);
    $('.qtyplus').click(function (e) {
        e.preventDefault();
        fieldName = $(this).attr('field');
        costName = fieldName + "-cost";
        var currentVal = parseInt($('input[name=' + fieldName + ']').val());
        var costVal = parseInt($('input[name=' + costName + ']').val());
        if (currentVal !== maxStat) {
            if (pointsToSpend === 0) {
                $('input[name=' + fieldName + ']').val(currentVal + 0);
                $('input[name=' + costName + ']').val(costVal + 0);
            } else if (pointsToSpend === 1) {
                if (currentVal >= 14) {
                } else {
                    $('input[name=' + fieldName + ']').val(currentVal + 1);
                    $('input[name=' + costName + ']').val(costVal + 1);
                    pointsToSpend -= 1;
                }
            } else if (!isNaN(currentVal)) {
                currentVal = currentVal + 1;
                if (currentVal === 14) {
                    pointsToSpend -= 2;
                    costVal = costVal + 2;
                } else if (currentVal === 15) {
                    pointsToSpend -= 2;
                    costVal = costVal + 2;
                } else {
                    pointsToSpend -= 1;
                    costVal = costVal + 1;
                }
                $('input[name=' + fieldName + ']').val(currentVal);
                $('input[name=' + costName + ']').val(costVal);
            } else {
                $('input[name=' + fieldName + ']').val(0);
                $('input[name=' + costName + ']').val(0);
            }
        }
        $('.pointsToSpend').text("");
        $('.pointsToSpend').append(pointsToSpend);
    });
    $(".qtyminus").click(function (e) {
        e.preventDefault();
        fieldName = $(this).attr('field');
        costName = fieldName + "-cost";
        var currentVal = parseInt($('input[name=' + fieldName + ']').val());
        var costVal = parseInt($('input[name=' + costName + ']').val());
        if (!isNaN(currentVal) && currentVal > 8) {
            if (currentVal === 14) {
                pointsToSpend += 2;
                costVal -= 2;
            } else if (currentVal === 15) {
                pointsToSpend += 2;
                costVal -= 2;
            } else {
                pointsToSpend += 1;
                costVal -= 1;
            }
            $('input[name=' + fieldName + ']').val(currentVal - 1);
            $('input[name=' + costName + ']').val(costVal);
        } else {
            $('input[name=' + fieldName + ']').val(8);
            $('input[name=' + costName + ']').val(0);
        }

        $('.pointsToSpend').text("");
        $('.pointsToSpend').append(pointsToSpend);
    });
    $('.qtyplus,.qtyminus').click(function (e) {
        fieldName = $(this).attr('field');
        modName = fieldName + "-modifier";
        scoreName = fieldName + "-score";
        var currentVal = parseInt($('input[name=' + fieldName + ']').val());


    });
});