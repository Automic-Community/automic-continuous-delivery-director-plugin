package com.automic.ara.cdd.plugin.utils;

public class Constants {

    public static final int TASK_POLL_DELAY = 10;

    public static final String TASK_OUTPUT_PARAMETER_RUN_ID = "RunID";
    public static final String TASK_OUTPUT_PARAMETER_EXECUTION_ID = "ExecutionID";
    public static final String TASK_OUTPUT_PARAMETER_COMPONENTS = "Components";
    public static final String TASK_OUTPUT_PARAMETER_INSTALLATIONS = "Installations";

    public static final String TASK_CONTEXT_KEY_EXECUTION_ID = "executionId";
    public static final String TASK_CONTEXT_KEY_COMPENSATION_EXECUTION_ID = "compensationExecutionId";

    public static final String TIME_OUTPUT_FORMAT = "hh:mm:ss a";
    public static final String ICON_FORMAT = "<a href=\"%s\" target=\"_blank\"><img style=\"vertical-align: middle;\" src=\"%s\" alt=\"%s\"></a>";
    public static final String ROW_FORMAT = "<div class=\"layout-row\">" +
                                                "<div class='layout-align-start-center layout-row' style=\"width:150px; height:26px\">%s:</div>" +
                                                "<div class='layout-align-start-center layout-row'>%s</div>" +
                                            "</div>";

    public static final String BASE64_INSTALLATION_ICON = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAFo9M/3AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAqxJREFUeNpi/P//PwMc3Ph25j9AADHCRFiOHj36/9mzZwwAAcQAEgFijo4DTQtBbMaFCxeC1UgpKTEI8fIwAAQQI4opQMAEIgIn+l9ya3aLBbGZdXR0/j/6/UBcXUAz8MXNN/mMFy9d+v/u82eGr99/MEgKCzEABBCGGeiAEUQAFTF6tLn/M7XQY9CV02QQZhVneP7wfXysfewixu6+/g+amhr8snJyDKoqygyrjq1icNfzZrh67TrDsf37IO4Wl5Bk+A+CQNtQ6H//GQACiKAbmBgIABaYYz1bPaM09JXCvr7+yTgrcU4AUOwf2ARGRkaGA1cPBOjoqS+Rl5Dyi/Dw9w2bEPIXJA7GS5aveMfM8F/w8rfLDE4eFgx7jx1m8Jb0Z3j+/DlDaGgoI9O927cE5YEhXxRcxvDmwU+GQvcqBlYeXgZGdg4GUBAwXrp86T/IJ6/ff2BgZGJm+AtkP79/j0FHX4/hysVLDCzPnr9ggHkVErkMDMJiYgzPngHFgWIAAcYEjXGyMQu2sHNpdPHmE+RxVFSW2yfFJ/325q07Ercf33m9v2H/UZBD8AU0R/mS8tXMzMzz/v3/l8XJwbaIie3vJiYWhgXMjMxb8ufnbwKlMZSIAIXl8nUb8n59/jhRWVmZge8/HwMnJweDg7kFg5SIOLu8sByDu7Yrw4QVMxn4Gfl9gKn9z927dxnYePnzI4MCJjFOmzOvlI3hXxcfPz+Dta0tAxs7OwM3FxfD9F1TGV59f8bAx8XDwPaXhyHTPYfhy9evDN9+/GA4dfQow5dPnxh+MTBVw3MJMjC1smIAGcjKzAJO0T9+/WZ49+4dw6XTp7CnJRVVFUg0AfHrV68ZTh87hjXh8fLxMQiJiECj9B/Dw/sPIAbcuX2HgRjwGehsEIZnN0YmBgDdv0ODBcmy1wAAAABJRU5ErkJggg==";
    public static final String BASE64_MONITORING_ICON = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA7FpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYxIDY0LjE0MDk0OSwgMjAxMC8xMi8wNy0xMDo1NzowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wUmlnaHRzPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvcmlnaHRzLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcFJpZ2h0czpNYXJrZWQ9IkZhbHNlIiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InV1aWQ6RkNDOTVBNzVFRjM3REYxMTlBNjlEQTI4RUMwNUIyREEiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6QTQyMzJCRTIxNzg4MTFFMkFDODE5RDdEMDU1M0NFNTEiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6QTQyMzJCRTExNzg4MTFFMkFDODE5RDdEMDU1M0NFNTEiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNS4xIE1hY2ludG9zaCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjAxODAxMTc0MDcyMDY4MTE4NzFGRUVBRUQ2MURBMTdDIiBzdFJlZjpkb2N1bWVudElEPSJ1dWlkOkZDQzk1QTc1RUYzN0RGMTE5QTY5REEyOEVDMDVCMkRBIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+YXlIQAAAAnxJREFUeNqEUl1IU2EYfr7zk6tjW61lKMYqiyGSEQUOIsLSimoX3mQXInWVF42uhAwi8MJroaDdSHQTmAXhFpZCdrOpiBmajZqWBVm0pTtzM9v5We85c2sTxAce3u/7zvs+33Pe9+MymQwMll8uP0X8TettRCF3vhk5ECqaK25QGPScbbBXXqm8WdVadZT2PNGCDeB0Ok0KlPyQCq9eajwDxjhwHNc19SHcRaLTkkXyRh5FQpSvrBcwbjcgqKo6+WLodUvVfqdg37UTSwkZx48dwckTdYd7+/xvAq8C6Hvch4WFhSIBl8tlRj4+HR8LjAYmh0OhpkQqKZ4+cB5L0RSGR4IQt3JoaWzF2MQsxt/PIbacLuL2Ega2ZsU6Mz/T7b3vvfak4xksUiliiSgudDRg5N5b+AYiUDUd1La8g3fBIfyY6M82kZCo2VeT6PH2YPbLPLr7Z+Cw7sb1i23gOeolg8m/io54Ko2fS3+wKCehKEregYn6+vrMMrOiqa0Tzj2lqDskIBhWEP4um8WaTiQnhpvZ8UHEpwIQChsTjcZgKbNCoYTpr3HIKxK+RVfM6ZSIjAoZ0gUTUFS1WEBRFazKy/glr2KLKJjFosAbo4VOtxvQ9Aw0RmvGzF8oEtA1jQ7T+DQ6AF7gIPIceJ6nPrBsofGdqOkq5TJzXyRQW1sLn8+30eOjQg13n98BExXcPteJW+2fs0202WzkiNkcDkccm4HmxniGjLL2EnPnkUhEzg7rPzwej41CTlT2+/071uvlHRSiurq6nUIzuSqTJGlv7u2nUqkJQ4j4IBwOP807kGW5SMDtdvdSWKQiezKZPFg4aeJH4stczT8BBgC+n0zou5g1qgAAAABJRU5ErkJggg==";
}
