import * as React from "./React.js";

export let Circle = React.createClass({
    render: function() {
        return(
            <svg width="200px" height="200px" className="center">
                <circle cx="100px" cy="100px" r="100px" fill={this.props.get("color")}>
                </circle>
            </svg>
        );
    }
});
