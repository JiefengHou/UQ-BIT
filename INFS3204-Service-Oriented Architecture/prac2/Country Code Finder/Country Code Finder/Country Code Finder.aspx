<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Country Code Finder.aspx.cs" Inherits="Country_Code_Finder.Country_Code_Finder" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
     <style type="text/css">
        #head {
            width:100%; 
            color:#ffffff;
            background-color:#336699;  
            font-family:'Times New Roman';
            width:100%;
            float:left;
            height:auto;
            
        }

        h1 {
            font-size:28px;
            font-weight:bold;
            float:left;
            margin:15px 10px;

        }

        .style {
            font-size:120%;
            font-weight:bolder;
        }

        #navigation {
            background-color:#666699;
            color:#E8E8E8;
            list-style: none;
            float:left;
            width:100%;
            
        }

        ul {
           float:left;
           margin:5px -30px;
        }

        li {
            display:inline;
            float:left;
            border-style:solid;
            border-width:1px;
            border-color:#E8E8E8;
            padding:5px 20px;
            
        }

        #title {
            float:left;
            width:100%;
            height:100%;
        }

        h3 {
            text-transform:uppercase;
            color:#663333;
            font-weight:normal;
            float:left;
            margin-left:10px;
        }

        .table-style {
            float:left;
            width:100%;
            color:#663333;
        }

        td {
            float:left;
            margin-top:10px;
            margin-left:10px;
        }
         .auto-style1 {
             width: 100%;
         }
         .auto-style2 {
             height: 24px;
         }
    </style>
    <title></title>
</head>
<body>
        <div>
       <div id="head"><h1><span class="style">M</span>Y <span class="style">ASP.NET A</span>PPLICATION</h1></div>
       <div id="navigation">
           <ul>
               <li>Home</li>
               <li>About</li>
           </ul>
       </div>
    </div>
    <form id="form1" runat="server">
    <div>
        <asp:ScriptManager ID="ScriptManager1" runat="server">
            <Services>
                    <asp:ServiceReference Path="~/Country Code Finder.asmx" />
            </Services>
        </asp:ScriptManager>
    </div>
    <div>  
        
        <table class="auto-style1">
            <tr>
                <td colspan="3" class="auto-style2">
                    <span class="style">T</span>HE FIRST RUN RECORDED AT:
                    <span class="style"><asp:Label ID="Label1" runat="server"></asp:Label></span>
                </td>
            </tr>

            <tr>
                <td class="auto-style2">
                    <asp:Label ID="Label2" runat="server" Text="Country:"></asp:Label>
                </td>
                <td class="auto-style2">
                    <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
                </td>
                <td class="auto-style2">
                    <input type="button" value="Find Country Code" onclick="onClick()"/>
                </td>
            </tr>

            <tr>
                <td colspan="3">
                    <asp:Label ID="Label3" runat="server"></asp:Label>
                </td>
            </tr>
        </table>       
    </div>
    </form>
    <script type="text/javascript">
        var onClick = function () {
            Country_Code_Finder.Country_Code_Finder1.findCountryCode($get("TextBox1").value, onSuccess, onFailed);
        }

        var onSuccess = function (result) {
            $get("Label3").innerHTML = result;
        }

        var onFailed = function (result) {
            $get("Label3").innerHTML = "There are something wrong with input";
        }
    </script>
</body>
</html>
