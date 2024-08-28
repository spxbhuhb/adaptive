/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.parse

val svg = """
<?xml version="1.0" encoding="UTF-8"?>
<svg id="thermometer-temperature-svgrepo-com" xmlns="http://www.w3.org/2000/svg" width="9.744" height="18.343" viewBox="0 0 9.744 18.343">
    <g id="Group_4" transform="translate(2.257)">
        <g id="Group_3">
            <path id="Path_1" d="M189.244,11.813V2.5a2.5,2.5,0,1,0-5,0v9.314a3.743,3.743,0,1,0,5,0ZM187.9,8.2h-2.313V2.5a1.156,1.156,0,1,1,2.313,0V8.2Z" transform="translate(-183.002)" fill="#1ad598"/>
        </g>
    </g>
    <g id="Group_6" transform="translate(0 8.501)">
        <g id="Group_5">
            <path id="Path_2" d="M121.815,237.268h-1.128a.671.671,0,0,0,0,1.342h1.128a.671.671,0,0,0,0-1.342Z" transform="translate(-120.016 -237.268)" fill="#1ad598"/>
        </g>
    </g>
    <g id="Group_8" transform="translate(0 6.29)">
        <g id="Group_7">
            <path id="Path_3" d="M121.815,175.566h-1.128a.671.671,0,1,0,0,1.342h1.128a.671.671,0,1,0,0-1.342Z" transform="translate(-120.016 -175.566)" fill="#1ad598"/>
        </g>
    </g>
    <g id="Group_10" transform="translate(0 4.079)">
        <g id="Group_9">
            <path id="Path_4" d="M121.815,113.864h-1.128a.671.671,0,0,0,0,1.342h1.128a.671.671,0,0,0,0-1.342Z" transform="translate(-120.016 -113.864)" fill="#1ad598"/>
        </g>
    </g>
</svg>
""".trimIndent()

val svg2 = """
<svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#e8eaed">
    <path d="M184.62-200q-27.62 0-46.12-18.5Q120-237 120-264.62v-430.76q0-27.62 18.5-46.12Q157-760 184.62-760h590.76q27.62 0 46.12 18.5Q840-723 840-695.38v430.76q0 27.62-18.5 46.12Q803-200 775.38-200H184.62ZM480-475.38 160-684.62v420q0 10.77 6.92 17.7 6.93 6.92 17.7 6.92h590.76q10.77 0 17.7-6.92 6.92-6.93 6.92-17.7v-420L480-475.38Zm0-44.62 307.69-200H172.31L480-520ZM160-684.62V-720v455.38q0 10.77 6.92 17.7 6.93 6.92 17.7 6.92H160v-444.62Z"/>
</svg>
""".trimIndent()