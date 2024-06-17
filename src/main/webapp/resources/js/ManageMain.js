// 예제 데이터
$(document).ready(function() {
    document.getElementById('today-signups').innerText
    $.ajax({
        url: '/Manage/todayRegisterUser',
        type: 'GET',
        processData: false,
        contentType: false,
        dataType: 'json',
        success: function(result) {
            const today_signup=document.getElementById('today-signups');
            today_signup.innerText=result;
        },
        error: function(xhr, status, error) {
            console.log("응답 내용:", xhr.responseText);
        }});
    $.ajax({
        url: '/Manage/getEventList',
        type: 'GET',
        processData: false,
        contentType: false,
        dataType: 'json',
        success: function(result) {
            const eventList = document.getElementById('event-list');
            result.forEach(event => {
                const li = document.createElement('li');
                li.className = 'list-group-item';
                li.innerText = `${event.evt_no} ${event.title} 종료일 : ${event.e_date}`;
                eventList.appendChild(li);
            });
        },
        error: function(xhr, status, error) {
            console.log("응답 내용:", xhr.responseText);
        }
    });
    $.ajax({
        url: '/Manage/getCharImfo',
        type: 'GET',
        processData: false,
        contentType: false,
        dataType: 'json',
        success: function(result) {
            result.forEach(function(d) {
                d.total_view_cnt = +d.total_view_cnt; // 문자열을 숫자로 변환
            });
            drawChart(result);
        },
        error: function(xhr, status, error) {
            console.log("응답 내용:", xhr.responseText);
        }
    });
});
const signupsToday = 10;
const cancellationsToday = 2;


const pendingInquiries = [
    'Inquiry 1: Details...',
    'Inquiry 2: Details...',
    'Inquiry 3: Details...'
];


// DOM 요소 업데이트

document.getElementById('today-cancellations').innerText = cancellationsToday;

const inquiryList = document.getElementById('inquiry-list');
pendingInquiries.forEach(inquiry => {
    const li = document.createElement('li');
    li.className = 'list-group-item';
    li.innerText = inquiry;
    inquiryList.appendChild(li);
});

const eventList = document.getElementById('event-list');

var drawChart = function(ChartData) {
    // 차트 생성
    const svg = d3.select('#category-chart').append('svg')
        .attr('width', '100%')
        .attr('height', 400);

    const margin = { top: 20, right: 20, bottom: 30, left: 40 };
    const width = document.getElementById('category-chart').clientWidth - margin.left - margin.right;
    const height = +svg.attr('height') - margin.top - margin.bottom;
    const g = svg.append('g').attr('transform', `translate(${margin.left},${margin.top})`);

    const x = d3.scaleBand()
        .rangeRound([0, width])
        .padding(0.1)
        .domain(ChartData.map(d => d.name));

    const yMax = d3.max(ChartData, d => d.total_view_cnt);
    const y = d3.scaleLinear()
        .rangeRound([height, 0])
        .domain([0, yMax * 1.2]); // y축의 max값을 데이터의 max값보다 20% 더 크게 설정

    g.append('g')
        .attr('class', 'axis axis--x')
        .attr('transform', `translate(0,${height})`)
        .call(d3.axisBottom(x));

    g.append('g')
        .attr('class', 'axis axis--y')
        .call(d3.axisLeft(y).ticks(10).tickFormat(d3.format("d"))); // 정수 포맷으로 눈금 표시

    g.selectAll('.bar')
        .data(ChartData)
        .enter().append('rect')
        .attr('class', 'bar')
        .attr('x', d => x(d.name))
        .attr('y', d => y(d.total_view_cnt))
        .attr('width', x.bandwidth())
        .attr('height', d => height - y(d.total_view_cnt))
        .on('mouseover', function(event, d) {
            const tooltip = d3.select('#tooltip');
            const xPosition = parseFloat(d3.select(this).attr('x')) + x.bandwidth() / 2;
            const yPosition = parseFloat(d3.select(this).attr('y')) - 10;

            tooltip.style('opacity', 1)
                .style('left', `${xPosition + margin.left}px`)
                .style('top', `${yPosition + margin.top}px`)
                .text(d.total_view_cnt);

            d3.select(this).attr('fill', 'orange');
        })
        .on('mouseout', function() {
            d3.select('#tooltip').style('opacity', 0);
            d3.select(this).attr('fill', 'steelblue');
        })
        .attr('fill', 'steelblue');
}