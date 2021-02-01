(function($) {

    var YearSelect =  function(el, settings) {
        this.$el = el;
        this.options = $.extend({}, $.fn.yearselect.defaults, settings);
        this.start = this.options.start || this.start;
        this.end = this.options.end || this.end;
        this.step = this.options.step || this.step;
        this.order = this.options.order || this.order;
        this.selected = this.options.selected !== "current" || this.$el.data('selected') || this.selected
        this.displayAsValue = this.options.displayAsValue || this.$el.data('display-as-value') || this.displayAsValue;
        this.years = [];
        this.init();
    }

    YearSelect.prototype = {
        constructor: YearSelect,
        init: function() {
            order = this.order.toLowerCase();
            if(order == 'desc') {
                this.start = this.options.end;
                this.end = this.options.start;
            }
            this.destroy();
            this.render(order);
        },
        render: function(order) {
            order = order.toLowerCase();
            if(order == 'asc') {
                this.renderAscending();
            } else if(order == 'desc') {
                this.renderDescending();
            }
        },
        renderAscending: function() {
            for(var i = this.start; i <= this.end; i += this.step) {
                this.years.push(i);
                this.renderDisplay(i);
            }
        },
        renderDescending: function() {
            for(var i = this.start; i >= this.end; i -= this.step) {
                this.years.push(i);
                this.renderDisplay(i);
            }
        },
        renderDisplay: function(yr) {
            var customDisplay = this.formatDisplay(yr),
                val = this.displayAsValue ? customDisplay : yr,
                current = new Date().getFullYear();

            var el = $('<option>').html(customDisplay).val(val);
            this.$el.append(el);

            if(yr == this.selected
                || (this.selected == "current" && yr === current)) this.setSelected(val);
        },
        formatDisplay: function(yr) {
            if(typeof this.options.formatDisplay === "function") {
                return this.options.formatDisplay(yr)
            }
            return yr;
        },
        setSelected: function(value) {
            this.$el.val(value);
        },
        destroy: function() {
            this.years = [];
            this.$el.html('');
        }
    }

    $.fn.yearselect = function(option) {
        return this.each(function() {
            var yearselectel = $(this);

            // if element is not a select element,
            // transform it to select element
            // with the original data intact
            if(!yearselectel.is('select')) {
                var el_data = {
                    'name' : yearselectel.attr('name') || yearselectel.data('name') || '',
                    'class' : yearselectel.attr('class') || yearselectel.data('class') || '',
                    'id' : yearselectel.attr('id') || yearselectel.data('id') || '',
                    'selected' : yearselectel.attr('value') || yearselectel.data('selected') || '',
                }

                yearselectel = $('<select class="yearselect"></select>')
                    .attr('name', el_data.name)
                    .attr('class', el_data.class)
                    .attr('id', el_data.id)
                    .data('selected', el_data.selected);
                $(this).replaceWith(yearselectel);
            }

            new YearSelect(yearselectel, option);
        });
    }

    $.fn.yearselect.defaults = {
        start: 1970,
        end: new Date().getFullYear(),
        step: 1,
        order: 'asc',
        selected: "current",
        formatDisplay: null,
        displayAsValue: true
    };

}(jQuery));