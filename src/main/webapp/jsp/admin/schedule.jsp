<%@ page contentType="text/html; charset=UTF-8" %>
<% //global variables
    var numDep=6;
    var numWorks=5;
    var startHour=5;
    var startMinute=0;
    var endHour=18;
    var endMinute=0;
    var timeStep=30;
%>
<html>
    <head>
        <title>Gestione orari</title>
        <link href="../../style/schedule.css" rel="stylesheet" type="text/css">
        <script>
            function parseInt(array) { //parse string array as integer array

                let newArr=Array(array.length); //new array instance
                let i=0;
                for (let item of array) { //loop over every item of old array

                    item++;
                    item--; //perform integer-only operations to parse as integer
                    newArr[i]=item; //assign item to array
                    i++;
                }

                return newArr; //return parsed array
            }

            function calculateTimeDuration(start, end) { //calculate total time slot duration

                let startTime=parseInt(start.split("-")); //parse values as numbers
                let endTime=parseInt(end.split("-"));

                let hour=endTime[0]-startTime[0]; //get hour duration
                let minute=(endTime[1]-startTime[1])/<%= timeStep %>; //get minute duration
                if (minute<0) { //count for time slots of non-integer hour span

                    minute=(60/<%= timeStep %>)+minute;
                    hour--;
                }

                return (60/<%= timeStep %>)*hour+minute+1; //calculate size for table element
            }

            function checkAssignment(dep, size, timeSlot) { //check for time slot to be assignable

                if (size===1) return false; //disallow single time slot insertions
                for (let i=0; i<size-1; i++) { //loop over duration of time slot

                    let newTag=timeSlot[0]+"-"+timeSlot[1]+(timeSlot[1]===0 ? "0" : ""); //generate tag for table element
                    let nextSlot=document.querySelector("#table\\["+dep+"\\]\\["+newTag+"\\]"); //search for element by tag
                    if (nextSlot && (nextSlot.hasAttribute("hidden") || nextSlot.hasAttribute("rowspan"))) { //check for already assigned time slots

                        console.log("Time slot already in work");
                        return false; //time slot is already assigned and not available
                    }

                    timeSlot[1]+=<%= timeStep %>; //select next element
                    if (timeSlot[1]>=60) { //count for hour pass

                        timeSlot[1]=0; //reset minutes
                        timeSlot[0]++; //add hour
                    }
                }

                return true; //time slot is still free in every element
            }

            function setTimeSlot(dep, size, timeSlot, element) { //assign new work in table

                element.setAttribute("class", "addedWork"); //style with custom CSS
                element.setAttribute("rowspan", ""+size) //apply table element size
                element.innerHTML=document.querySelector("#workName").value; //apply selected work name

                for (let i=0; i<size-1; i++) {

                    timeSlot[1]+=<%= timeStep %>; //select next element
                    if (timeSlot[1]>=60) { //count for hour pass

                        timeSlot[1] = 0;
                        timeSlot[0]++;
                    }

                    let newTag=timeSlot[0]+"-"+timeSlot[1]+(timeSlot[1]===0 ? "0" : ""); //generate tag for table element
                    let nextSlot=document.querySelector("#table\\["+dep+"\\]\\["+newTag+"\\]"); //search for element by tag
                    nextSlot.setAttribute("hidden", "hidden");
                }
            }

            function addTimeSlot() { //add new work in table

                let depId=document.querySelector("#depList").querySelector(":checked").id;
                let startHour=document.querySelector("#startHour").querySelector(":checked").value.replace(":", "-");
                let duration=document.querySelector("#endHour").querySelector(":checked").value.replace(":", "-");
                let tableElement=document.querySelector("#table\\["+depId+"\\]\\["+startHour+"\\]");
                let totalDuration=calculateTimeDuration(startHour, duration);
                let isAssignable=checkAssignment(depId, totalDuration, parseInt(startHour.split("-"))); //check for time slot to be available
                if (isAssignable) setTimeSlot(depId, totalDuration, parseInt(startHour.split("-")), tableElement); //assign new work
            }

            function clearTable() { //reset table from every assigned work

                let works=document.querySelectorAll(".addedWork");
                let hiddenSlots=document.querySelectorAll("[hidden='hidden']");
                works.forEach(item => { //remove properties from work slots

                    item.removeAttribute("rowspan");
                    item.removeAttribute("class");
                    item.innerHTML="---";
                });
                hiddenSlots.forEach(item => item.removeAttribute("hidden")); //remove properties from hidden slots
            }

            function getDuration() { //get duration for event

                let startTime=document.querySelector("#startHour").querySelector(":checked").value.split(":");
                let timeLength=document.querySelector("#endHour");
                while (timeLength.firstChild) timeLength.removeChild(timeLength.lastChild);

                let start=parseInt(startTime);
                for (let hour=start[0]; hour<=<%= endHour %>; hour++) {

                    for (let minute=(hour===start[0] ? start[1] : 0); minute<(hour===<%= endHour %> ? <%= endMinute+timeStep %> : 60); minute+=<%= timeStep %>) {

                        let newOpt=document.createElement("option");
                        newOpt.text="" + hour + ":" + minute + (minute === 0 ? "0" : "");
                        timeLength.appendChild(newOpt);
                    }
                }
            }

            function onLoadHandler() { //event listener loader

                let addWorkButton=document.querySelector("#addTimeSlot");
                let removeWorksButton=document.querySelector("#clearTable");
                let startTimeLength=document.querySelector("#startHour");
                let endTimeLength=document.querySelector("#endHour");
                let managePeople=document.querySelector("#managePeople");

                addWorkButton.addEventListener("click", addTimeSlot);
                removeWorksButton.addEventListener("click", clearTable);
                startTimeLength.addEventListener("change", getDuration);
                endTimeLength.addEventListener("focus", getDuration);
                managePeople.addEventListener("click", function() { window.location.href="./people/people.jsp"; });

                getDuration(); //initial data generation
            }

            window.addEventListener("load", onLoadHandler);
        </script>
    </head>
    <body>
        <nav>
            <input type="button" id="managePeople" value="Lista persone">
        </nav>
        <form>
            <label for="depList">Selezionare dipendente: </label>
            <select name="depList" id="depList">
                <% for (var index=0; index<numDep; index++) { %><option id="d<%= index %>">Dipendente <%= index %></option><% } %>
            </select><br/>
            <label for="startHour">Selezionare orario inizio servizio: </label>
            <select id="startHour">
                <% for (var minute=(startHour*60+startMinute); minute<=(endHour*60+endMinute); minute+=timeStep) { %>
                <option><%=minute/60%>:<%=minute%60%><%=(minute%60)==0 ? "0" : ""%></option>
                <% } %>
            </select><br/>
            <label for="endHour">Selezionare orario fine evento: </label>
            <select id="endHour"></select><br/>
            <label for="workName">Selezionare nome evento: </label>
            <select id="workName">
                <% for (var workIndex=0; workIndex<numWorks; workIndex++) { %>
                <option>Lavoro <%= workIndex %></option>
                <% } %>
            </select><br/><br/>
            <input type="button" value="Aggiungi evento" id="addTimeSlot">
            <input type="button" value="Pulisci tabella" id="clearTable">
        </form>
    <table>
        <tr>
            <td colspan="<%= numDep+1 %>" id="tableTitle">
                <label for="weekDay">Selezionare il giorno: </label>
                <input type="date" id="weekDay">
            </td>
        </tr>
        <tr>
            <td>Orario</td>
            <% for (var index=0; index<numDep; index++) { %><td>Dipendente <%= index %></td><% } %>
        </tr>
        <% for (var minute=(startHour*60+startMinute); minute<=(endHour*60+endMinute); minute+=timeStep) { %>
        <tr>
            <td><%=minute/60%>:<%=minute%60%><%=(minute%60)==0 ? "0" : ""%></td>
            <% for (var x=0; x<numDep; x++) { %><td id="table[d<%= x %>][<%=minute/60%>-<%=minute%60%><%=(minute%60)==0 ? "0" : ""%>]">---</td><% } %>
        </tr>
        <% } %>
    </table>
    </body>
</html>