// 예제 데이터
$(document).ready(function() {
    console.log("Document is ready");

    $.ajax({
        url: '/Manage/getEventList',
        type: 'GET',
        processData: false,
        contentType: false,
        dataType: 'json',
        success: function(result) {
            console.log("AJAX 요청 성공", result);
            const eventList = document.getElementById('event-list');
            result.forEach(event => {
                const li = document.createElement('li');
                li.className = 'list-group-item';
                li.innerText = `${event.evt_no} ${event.title} 종료일 : ${event.e_date}`;
                eventList.appendChild(li);
            });
        },
        error: function(xhr, status, error) {
            console.error("AJAX 요청 실패", status, error);
            console.log("응답 내용:", xhr.responseText);
        }
    });
});
const signupsToday = 10;
const cancellationsToday = 2;
const categoryData = [
    { category: 'Category 1', views: 100 },
    { category: 'Category 2', views: 150 },
    { category: 'Category 3', views: 200 },
    { category: 'Category 4', views: 80 },
    { category: 'Category 5', views: 80},
    { category: 'Category 6', views: 100 },
    { category: 'Category 7', views: 150 },
    { category: 'Category 8', views: 200 },
    { category: 'Category 9', views: 80 },
    { category: 'Category 10', views: 80},
    { category: 'Category 11', views: 100 },
    { category: 'Category 12', views: 150 },
    { category: 'Category 13', views: 200 },
    { category: 'Category 14', views: 80 },
    { category: 'Category 15', views: 80},
    { category: 'Category 16', views: 100 },
    { category: 'Category 17', views: 150 },
    { category: 'Category 18', views: 200 },
    { category: 'Category 19', views: 80 },
    { category: 'Category 20', views: 80},
    { category: 'Category 21', views: 100 },
    { category: 'Category 22', views: 150 },
    { category: 'Category 23', views: 200 },
    { category: 'Category 24', views: 80 },
    { category: 'Category 25', views: 80}
];

const pendingInquiries = [
    'Inquiry 1: Details...',
    'Inquiry 2: Details...',
    'Inquiry 3: Details...'
];


// DOM 요소 업데이트
document.getElementById('today-signups').innerText = signupsToday;
document.getElementById('today-cancellations').innerText = cancellationsToday;

const inquiryList = document.getElementById('inquiry-list');
pendingInquiries.forEach(inquiry => {
    const li = document.createElement('li');
    li.className = 'list-group-item';
    li.innerText = inquiry;
    inquiryList.appendChild(li);
});

const eventList = document.getElementById('event-list');


// D3.js를 이용한 바 차트 생성
const svg = d3.select('#category-chart').append('svg')
    .attr('width', '100%')
    .attr('height', 400);

const margin = { top: 20, right: 20, bottom: 30, left: 20 };
const width = document.getElementById('category-chart').clientWidth - margin.left - margin.right;
const height = +svg.attr('height') - margin.top - margin.bottom;
const g = svg.append('g').attr('transform', `translate(${margin.left},${margin.top})`);

const x = d3.scaleBand()
    .rangeRound([0, width])
    .padding(0.1)
    .domain(categoryData.map(d => d.category));

const y = d3.scaleLinear()
    .rangeRound([height, 0])
    .domain([0, d3.max(categoryData, d => d.views)]);

g.append('g')
    .attr('class', 'axis axis--x')
    .attr('transform', `translate(0,${height})`)
    .call(d3.axisBottom(x));

g.append('g')
    .attr('class', 'axis axis--y')
    .call(d3.axisLeft(y).ticks(10));

g.selectAll('.bar')
    .data(categoryData)
    .enter().append('rect')
    .attr('class', 'bar')
    .attr('x', d => x(d.category))
    .attr('y', d => y(d.views))
    .attr('width', x.bandwidth())
    .attr('height', d => height - y(d.views))
    .on('mouseover', function(event, d) {
        const tooltip = d3.select('#tooltip');
        const xPosition = parseFloat(d3.select(this).attr('x')) + x.bandwidth() / 2;
        const yPosition = parseFloat(d3.select(this).attr('y')) - 10;

        tooltip.style('opacity', 1)
            .style('left', `${xPosition + margin.left}px`)
            .style('top', `${yPosition + margin.top}px`)
            .text(d.views);

        d3.select(this).attr('fill', 'orange');
    })
    .on('mouseout', function() {
        d3.select('#tooltip').style('opacity', 0);
        d3.select(this).attr('fill', 'steelblue');
    })
    .attr('fill', 'steelblue');
