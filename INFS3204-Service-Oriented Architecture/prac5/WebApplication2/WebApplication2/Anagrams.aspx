<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Anagrams.aspx.cs" Inherits="WebApplication2.WebForm1" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
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
           width: 1246px;
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
            font-size:150%;
            font-weight:bolder;
        }

       </style>
</head>
<body>

    <form id="form1" runat="server">
    <div>
       <div id="head"><h1><span class="style">M</span>Y <span class="style">ASP.NET A</span>PPLICATION</h1></div>
       <div id="navigation">
           <ul>
               <li><asp:Hyperlink ID="Hyperlink1" runat="server" NavigateUrl="Anagrams.aspx">Anagrams Page</asp:Hyperlink></li>
               <li><asp:Hyperlink ID="Hyperlink2" runat="server" NavigateUrl="ASCII Value Filter.aspx">ASCII Value Filter Page</asp:Hyperlink></li>
           </ul>
       </div>
    </div>

    <div id="title">
        <h3>Anagrams Page:</h3>
    </div>

    <div>
    
        <asp:Label ID="Label1" runat="server" Text="Input"></asp:Label>
&nbsp;&nbsp;&nbsp;
        <asp:TextBox ID="TextBox1" runat="server" Width="719px"></asp:TextBox>
&nbsp;&nbsp;
        <asp:Button ID="Button1" runat="server" OnClick="Button1_Click" Text="Find" />
        <br />
        <br />
        <asp:Label ID="Label2" runat="server" Text="Output"></asp:Label>
&nbsp;&nbsp;&nbsp;
        <asp:Label ID="Label3" runat="server"></asp:Label>
    
    </div>
    </form>
</body>
</html>
