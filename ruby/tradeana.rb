require 'open-uri'

filename = "alldataUSD.csv"

if File.exist?(filename)
  puts("Overwrite(w) #{filename} or append(a)?")
  fileMode = STDIN.gets
  file = File.open("alldata.csv", "a") if fileMode.chop == "a"
  file = File.open("alldata.csv", "w") if fileMode.chop == "w"
else
  file = File.open("alldata.csv", "w")
end

if ARGV[0] == nil
  if fileMode == "a"
    puts "Do you really want to append new data to old file? (y/n)"
    answer = STDIN.gets
    exit 1 if answer != "y"
  end
  startDate = (Time.now - (60 * 60 * 24))
  endDate = Time.now
else
  startDate = Time.at(ARGV[0].to_i)
  endDate = startDate - (60 * 60 * 24)
end


while true
  call = "http://bitcoincharts.com/charts/chart.json?m=mtgoxUSD&SubmitButton=Draw&r=1&i=1-min&c=1&s=#{startDate.strftime("%F")}&e=#{endDate.strftime("%F")}&Prev=&Next=&t=S&b=&a1=&m1=10&a2=&m2=25&x=0&i1=&i2=&i3=&i4=&v=1&cv=0&ps=0&l=0&p=0&"
  #call = "http://api.bitcoincharts.com/v1/trades.csv?symbol=mtgoxUSD&end=#{now.to_i}&start=#{now.to_i - (3600 *3)}"
  puts call
  result = URI.parse(call).read
  result = result + "\n"
  #puts result
  file.write(result)
  startDate = startDate - (60*60*24)
  endDate = endDate - (60*60*24)
end

file.close
