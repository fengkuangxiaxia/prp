$(document).ready(function(){
	$('.fancybox').fancybox({
		padding: 0,
		minHeight : 500,
		maxHeight : 500,
		closeBtn  : false,
		closeClick : true,
		helpers : {
			title : {
				type : 'outside'
			},
		}
	});
	$('.fancybox[rel*=bookimage]').fancybox({
		padding: 0,
		prevEffect : 'none',
		nextEffect : 'none',
		minHeight : 500,
		maxHeight : 500,
		closeBtn  : false,
		arrows    : false,
		nextClick : true,
		helpers : {
			title : {
				type : 'outside'
			},
			thumbs : {
				width  : 50,
				height : 50
			}
		}
	});
});